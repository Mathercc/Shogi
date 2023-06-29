
//負責處理盤面上棋子的移動方式
public class Move extends Action{
    
    //判斷是否能移動
    public static boolean canMove(int from,int to,char piece){
        //建立一個虛擬盤面
        StringBuilder tempMap = new StringBuilder(map);
        //如果上一次點擊不是持駒且與此次點擊不是同一方的棋子
        if(from!=81&&(btns[from].getForeground()!=btns[to].getForeground())){
            //輪到先手時，判斷所有先手的棋子
            if(turn%2==0&&Character.isUpperCase(piece)){
                //判斷是否符合其移動模式
                if(fitMoveMode(from,to,piece,tempMap)){
                    //假設已經移動過後
                    tempMap.setCharAt(to,piece);
                    tempMap.setCharAt(from,' ');
                    int king = tempMap.indexOf("K");
                    for(int i=0;i<81;i++){
                        //判斷後手的棋子是否可以吃掉自己的玉將
                        if(Character.isLowerCase(tempMap.charAt(i))){
                            if(fitMoveMode(i,king,tempMap.charAt(i),tempMap)){
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
            //輪到後手時，判斷所有後手的棋子
            if(turn%2==1&&Character.isLowerCase(piece)){
                //判斷是否符合其移動模式
                if(fitMoveMode(from,to,piece,tempMap)){
                    //假設已經移動過後
                    tempMap.setCharAt(to,piece);
                    tempMap.setCharAt(from,' ');
                    int king = tempMap.indexOf("k");
                    for(int i=0;i<81;i++){
                        //判斷先手的棋子是否可以吃掉自己的玉將
                        if(Character.isUpperCase(tempMap.charAt(i))){
                            if(fitMoveMode(i,king,tempMap.charAt(i),tempMap)){
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    //判斷是否能打入
    public static boolean canSet(int to,char piece){
        //如果上次點擊的是持駒
        if(piece==' ') return false;
        //先建立一個虛擬盤面
        StringBuilder tempMap = new StringBuilder(map);
        //如果該格沒有棋子
        if(map.charAt(to)==' '){
            //如果是先手的持駒(被吃掉的後手棋子)
            if(Character.isLowerCase(piece)){
                //假設已經移動過後
                tempMap.setCharAt(to,Character.toUpperCase(piece));
                int king = tempMap.indexOf("K");
                for(int i=0;i<81;i++){
                    //判斷後手的棋子是否可以吃掉自己的玉將
                    if(Character.isLowerCase(tempMap.charAt(i))){
                        if(fitMoveMode(i,king,tempMap.charAt(i),tempMap)){
                            return false;
                        }
                    }
                }
            }
            //如果是後手的持駒(被吃掉的先手棋子)
            if(Character.isUpperCase(piece)){
                //假設已經移動過後
                tempMap.setCharAt(to,Character.toLowerCase(piece));
                int king = tempMap.indexOf("k");
                for(int i=0;i<81;i++){
                    //判斷先手的棋子是否可以吃掉自己的玉將
                    if(Character.isUpperCase(tempMap.charAt(i))){
                        if(fitMoveMode(i,king,tempMap.charAt(i),tempMap)){
                            return false;
                        }
                    }
                }
            }
            //禁止打入無去處駒，步兵、香車不能打入在底段；桂馬不能打入在底段、次底段；
            switch(temp){
                case 'p':
                    if(to>8&&Trans.notHavePawn(to)) return true; 
                    break;
                case 'l':
                    if(to>8) return true; 
                    break;
                case 'n':
                    if(to>17) return true;
                    break;
                case 'P':
                    if(to<72&&Trans.notHavePawn(to)) return true; 
                    break;
                case 'L':
                    if(to<72) return true; 
                    break;
                case 'N':
                    if(to<63) return true;
                    break;
                default: return true;
            }
        }
        return false;
    }
    
    //各種棋子的移動模式
    public static boolean fitMoveMode(int from,int to,char piece,StringBuilder map){
        
        int x=9-from%9;     //移動之前的筋
        int y=from/9+1;     //移動之前的段
        int X=9-to%9;       //移動之後的筋
        int Y=to/9+1;       //移動之後的段
        
        // 棋子字母大寫先手，小寫後手
        // 步(Pp) 香(Ll) 桂(Nn) 銀(Ss) 金(Gg) 角(Bb) 飛(Rr) 玉(Kk)
        // と(Tt) 杏(Aa) 圭(Jj) 全(Ww)        馬(Hh) 龍(Dd)
        
        //判斷前後位置是否不一樣
        if(from!=to){
            switch(piece){
                case 'P':   //正前方一格
                    return X-x==0&&Y-y==-1;
                case 'L':   //正前方無限格
                    if(X-x==0&&Y-y<0){
                        for(int i=y-1;i>Y;i--){
                            if(map.charAt(9*i-x)!=' '){
                                return false;
                            }
                        }
                        return true;
                    }else return false;
                case 'N':   //正前方兩格後再往左、右一格
                    return (X-x==1&&Y-y==-2)||(X-x==-1&&Y-y==-2);
                case 'S':   //正前、斜向一格
                    return (X-x==1&&Y-y==-1)||(X-x==-1&&Y-y==-1)||(X-x==1&&Y-y==1)||
                            (X-x==0&&Y-y==-1)||(X-x==-1&&Y-y==1);
                case 'G':  case 'T':  case 'A':  case 'J':  case 'W':   //斜前、直向一格
                    return (X-x==1&&Y-y==-1)||(X-x==0&&Y-y==-1)||(X-x==-1&&Y-y==-1)||
                            (X-x==1&&Y-y==0)||(X-x==-1&&Y-y==0)||(X-x==0&&Y-y==1);
                case 'K':  case 'k':    //直向、斜向一格
                    return (X-x==1&&Y-y==-1)||(X-x==0&&Y-y==-1)||(X-x==-1&&Y-y==-1)||(X-x==1&&Y-y==0)||
                            (X-x==-1&&Y-y==0)||(X-x==1&&Y-y==1)||(X-x==0&&Y-y==1)||(X-x==-1&&Y-y==1);
                case 'B':  case 'b':    //直向+斜向無限格
                    if(Math.abs(X-x)==Math.abs(Y-y)){
                        //左上
                        if(X-x>0&&Y-y<0){
                            int k=1;
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x-k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x-k)%9==8){
                                    return false;
                                }
                            }
                        }
                        //右上
                        if(X-x<0&&Y-y<0){
                            int k=1;
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x+k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x+k)%9==0){
                                    return false;
                                }
                            }
                        }
                        //左下
                        if(X-x>0&&Y-y>0){
                            int k=1;
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x-k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x-k)%9==8){
                                    return false;
                                }
                            }
                        }
                        //右下
                        if(X-x<0&&Y-y>0){
                            int k=1;
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x+k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x+k)%9==0){
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else return false;
                case 'R':  case 'r':    //直向無限格
                    if((X-x==0)||(Y-y==0)){
                        //上
                        if((X-x==0)&&(Y-y<0)){
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x)!=' '){
                                    return false;
                                }
                            }
                        }
                        //下
                        if((X-x==0)&&(Y-y>0)){
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x)!=' '){
                                    return false;
                                }
                            }
                        }
                        //左
                        if((X-x>0)&&(Y-y==0)){
                            for(int i=x+1;i<X;i++){
                                if(map.charAt(9*y-i)!=' '){
                                    return false;
                                }
                            }
                        }
                        //右
                        if((X-x<0)&&(Y-y==0)){
                            for(int i=x-1;i>X;i--){
                                if(map.charAt(9*y-i)!=' '){
                                    return false;
                                }
                            }
                        }
                        return true;
                    }else return false;
                case 'H':  case 'h':    //直向+斜向無限格、直向一格
                    if((X-x==0&&Y-y==-1)||(X-x==1&&Y-y==0)||(X-x==-1&&Y-y==0)||(X-x==0&&Y-y==1)) return true;
                    if(Math.abs(X-x)==Math.abs(Y-y)){
                        //左上
                        if(X-x>0&&Y-y<0){
                            int k=1;
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x-k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x-k)%9==8){
                                    return false;
                                }
                            }
                        }
                        //右上
                        if(X-x<0&&Y-y<0){
                            int k=1;
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x+k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x+k)%9==0){
                                    return false;
                                }
                            }
                        }
                        //左下
                        if(X-x>0&&Y-y>0){
                            int k=1;
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x-k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x-k)%9==8){
                                    return false;
                                }
                            }
                        }
                        //右下
                        if(X-x<0&&Y-y>0){
                            int k=1;
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x+k)!=' '){
                                    return false;
                                }
                                k++;
                                if((9*i-x+k)%9==0){
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else return false;
                case 'D':  case 'd':    //直向無限格、斜向一格
                    if((X-x==1&&Y-y==-1)||(X-x==-1&&Y-y==-1)||(X-x==1&&Y-y==1)||(X-x==-1&&Y-y==1)) return true;
                    if((X-x==0)||(Y-y==0)){
                        //上
                        if((X-x==0)&&(Y-y<0)){
                            for(int i=y-1;i>Y;i--){
                                if(map.charAt(9*i-x)!=' '){
                                    return false;
                                }
                            }
                        }
                        //下
                        if((X-x==0)&&(Y-y>0)){
                            for(int i=y+1;i<Y;i++){
                                if(map.charAt(9*i-x)!=' '){
                                    return false;
                                }
                            }
                        }
                        //左
                        if((X-x>0)&&(Y-y==0)){
                            for(int i=x+1;i<X;i++){
                                if(map.charAt(9*y-i)!=' '){
                                    return false;
                                }
                            }
                        }
                        //右
                        if((X-x<0)&&(Y-y==0)){
                            for(int i=x-1;i>X;i--){
                                if(map.charAt(9*y-i)!=' '){
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                    else return false;
                case 'p':   //正前方一格
                    return X-x==0&&Y-y==1;
                case 'l':   //正前方無限格
                    if(X-x==0&&Y-y>0){
                        for(int i=y+1;i<Y;i++){
                            if(map.charAt(9*i-x)!=' '){
                                return false;
                            }
                        }
                        return true;
                    }
                    else return false;
                case 'n':   //正前方兩格後再往左、右一格
                    return (X-x==1&&Y-y==2)||(X-x==-1&&Y-y==2);
                case 's':   //正前、斜向一格
                    return (X-x==1&&Y-y==-1)||(X-x==-1&&Y-y==-1)||(X-x==1&&Y-y==1)||
                            (X-x==0&&Y-y==1)||(X-x==-1&&Y-y==1);
                case 'g':  case 't':  case 'a':  case 'j':  case 'w':   //斜前、直向一格
                    return (X-x==0&&Y-y==-1)||(X-x==1&&Y-y==0)||(X-x==-1&&Y-y==0)||
                            (X-x==1&&Y-y==1)||(X-x==0&&Y-y==1)||(X-x==-1&&Y-y==1);
            }
        }
        return false;
    }
}