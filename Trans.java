
import java.awt.Color;

//負責程式內部資料間的轉換
public class Trans extends Move{
    
    //從map對應到board上的名稱轉換
    public static String transPiece(char before,char after){
        switch(after){
            case 'p':
            case 'P':
                return "步";
            case 'l':
            case 'L':
                return "香";
            case 'n':
            case 'N':
                return "桂";
            case 's':
            case 'S':
                return "銀";
            case 'g':
            case 'G':
                return "金";
            case 'k':
            case 'K':
                return "玉";
            case 'b':
            case 'B':
                return "角";
            case 'r':
            case 'R':
                return "飛";
            case 't':
            case 'T':
                if(before=='p'||before=='P') return "步成";
                else return "と";
            case 'a':
            case 'A':
                if(before=='l'||before=='L') return "香成";
                else return "成香";
            case 'j':
            case 'J':
                if(before=='n'||before=='N') return "桂成";
                else return "成桂";
            case 'w':
            case 'W':
                if(before=='s'||before=='S') return "銀成";
                else return "成銀";
            case 'h':
            case 'H':
                if(before=='b'||before=='B') return "角成";
                else return "馬";
            case 'd':
            case 'D':
                if(before=='r'||before=='R') return "飛成";
                else return "龍";
        }
        return "";
    }
    
    //數字轉換
    public static String transNum(int num){
        switch(num){
            case 1: return "一";
            case 2: return "二";
            case 3: return "三";
            case 4: return "四";
            case 5: return "五";
            case 6: return "六";
            case 7: return "七";
            case 8: return "八";
            case 9: return "九";
        }
        return "";
    }
    
    //大小寫轉換
    public static char transCase(char piece){
        if(Character.isLowerCase(piece)) return Character.toUpperCase(piece);
        if(Character.isUpperCase(piece)) return Character.toLowerCase(piece);
        return ' ';
    }
    
    //是否為打入
    public static String utsu(int from){
        if(from==81) return "打";
        else return "("+(9-from%9)+""+(from/9+1)+")";
    }
    
    //某筋是否沒有自己的步兵存在(禁止同筋二步)
    public static boolean notHavePawn(int to){
        if(Character.isLowerCase(temp)){
            for(int i=0;i<9;i++){
                if(map.charAt(to%9+9*i)==Character.toUpperCase(temp)) return false;
            }
        }
        if(Character.isUpperCase(temp)){
            for(int i=0;i<9;i++){
                if(map.charAt(to%9+9*i)==Character.toLowerCase(temp)) return false;
            }
        }
        return true;
    }
    
    //是否可以升級(穿梭敵陣判斷)
    public static boolean canPromote(char piece,int before,int after){
        return (piece=='p'||piece=='P'||piece=='l'||piece=='L'||
                piece=='n'||piece=='N'||piece=='s'||piece=='S'||
                piece=='b'||piece=='B'||piece=='r'||piece=='R')&&
                ((btns[lastClicked].getForeground()==Color.WHITE&&(before<=3||after<=3))||
                (btns[lastClicked].getForeground()==Color.BLACK&&(before>=7||after>=7)));
    }
    
    //是否必須升級(無去處駒判斷)
    public static boolean mustPromote(char piece,int dan){
        return (piece=='P')&&(dan==1)||(piece=='p')&&(dan==9)||
                (piece=='L')&&(dan==1)||(piece=='l')&&(dan==9)||
                (piece=='N')&&(dan<=2)||(piece=='n')&&(dan>=8);
    }
    
    //持駒種類與對應的序數
    public static int ordinal(char piece){
        if(piece=='P'||piece=='p') return 0;
        if(piece=='L'||piece=='l') return 1;
        if(piece=='N'||piece=='n') return 2;
        if(piece=='S'||piece=='s') return 3;
        if(piece=='G'||piece=='g') return 4;
        if(piece=='B'||piece=='b') return 5;
        if(piece=='R'||piece=='r') return 6;
        return 0;
    }
}
