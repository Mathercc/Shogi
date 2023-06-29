
import java.awt.*;
import javax.swing.*;

//負責顯示盤面信息
public class Map extends Shogi{
    
    //根據地圖字串顯示盤面
    public static void setBoard(JButton[] btns,StringBuilder map){
        for(int i=0;i<81;i++){
            JButton btn = btns[i];
            btn.setFont(new Font("標楷體", Font.PLAIN, 28));
            btn.setBackground(Color.decode("#db9356"));
            //根據map資訊顯示對應棋子到board
            switch(map.charAt(i)){
                case 'p':  btn.setText("步"); btn.setForeground(Color.BLACK); break;
                case 'P':  btn.setText("步"); btn.setForeground(Color.WHITE); break;
                case 'l':  btn.setText("香"); btn.setForeground(Color.BLACK); break;
                case 'L':  btn.setText("香"); btn.setForeground(Color.WHITE); break;
                case 'n':  btn.setText("桂"); btn.setForeground(Color.BLACK); break;
                case 'N':  btn.setText("桂"); btn.setForeground(Color.WHITE); break;
                case 's':  btn.setText("銀"); btn.setForeground(Color.BLACK); break;
                case 'S':  btn.setText("銀"); btn.setForeground(Color.WHITE); break;
                case 'g':  btn.setText("金"); btn.setForeground(Color.BLACK); break;
                case 'G':  btn.setText("金"); btn.setForeground(Color.WHITE); break;
                case 'k':  btn.setText("玉"); btn.setForeground(Color.BLACK); break;
                case 'K':  btn.setText("玉"); btn.setForeground(Color.WHITE); break;
                case 'r':  btn.setText("飛"); btn.setForeground(Color.BLACK); break;
                case 'R':  btn.setText("飛"); btn.setForeground(Color.WHITE); break;
                case 'b':  btn.setText("角"); btn.setForeground(Color.BLACK); break;
                case 'B':  btn.setText("角"); btn.setForeground(Color.WHITE); break;
                case 't':  btn.setText("と"); btn.setForeground(Color.BLACK); break;
                case 'T':  btn.setText("と"); btn.setForeground(Color.WHITE); break;
                case 'a':  btn.setText("杏"); btn.setForeground(Color.BLACK); break;
                case 'A':  btn.setText("杏"); btn.setForeground(Color.WHITE); break;
                case 'j':  btn.setText("圭"); btn.setForeground(Color.BLACK); break;
                case 'J':  btn.setText("圭"); btn.setForeground(Color.WHITE); break;
                case 'w':  btn.setText("全"); btn.setForeground(Color.BLACK); break;
                case 'W':  btn.setText("全"); btn.setForeground(Color.WHITE); break;
                case 'h':  btn.setText("馬"); btn.setForeground(Color.BLACK); break;
                case 'H':  btn.setText("馬"); btn.setForeground(Color.WHITE); break;
                case 'd':  btn.setText("龍"); btn.setForeground(Color.BLACK); break;
                case 'D':  btn.setText("龍"); btn.setForeground(Color.WHITE); break;
                default:   btn.setText(""); btn.setForeground(null);
            }
        }
    }
    
    //將吃到的棋子轉換為持駒
    public static void setHand(char piece){
        JButton[] hand = Character.isUpperCase(piece)?goteHand:senteHand;
        StringBuilder te = Character.isUpperCase(piece)?go:sen;
        int[] owner = Character.isUpperCase(piece)?g:s;
        int number = Trans.ordinal(piece);
        for(int i=0;i<7;i++){
            if(te.charAt(i)==piece) {
                owner[number]++;
                if(owner[number]==1) hand[i].setText(Trans.transPiece(piece,piece));
                else hand[i].setText(Trans.transPiece(piece,piece)+owner[number]);
                hand[i].setVisible(true);
                break;
            }
        }
    }
    
    //判斷是否將死
    public static boolean checkmate(StringBuilder map){
        //如果輪到先手
        if(turn%2==0){
            for(int i=0;i<81;i++){
                //判斷所有先手的棋子
                if(Character.isUpperCase(map.charAt(i))){
                    for(int j=0;j<81;j++){
                        //如果有任何一個棋子是可以移動的則還沒將死
                        if(Move.canMove(i,j,map.charAt(i))&&(btns[i].getForeground()!=btns[j].getForeground())){
                            return false;
                        }
                    }
                }
                //判斷所有先手的持駒
                for(int j=0;j<7;j++){
                    if(s[j]!=0){
                        //如果有任何一個棋子是可以打入的則還沒將死
                        if(Move.canSet(i,sen.charAt(j))&&(btns[i].getForeground()!=btns[j].getForeground())){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        //如果輪到後手
        if(turn%2==1){
            for(int i=0;i<81;i++){
                //判斷所有後手的棋子
                if(Character.isLowerCase(map.charAt(i))){
                    for(int j=0;j<81;j++){
                        //如果有任何一個棋子是可以移動的則還沒將死
                        if(Move.canMove(i,j,map.charAt(i))){
                            return false;
                        }
                    }
                }
                //判斷所有後手的持駒
                for(int j=0;j<7;j++){
                    if(g[j]!=0){
                        //如果有任何一個棋子是可以打入的則還沒將死
                        if(Move.canSet(i,go.charAt(j))){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    //重置盤面底色
    public static void clear(){
        for(int b=0;b<81;b++){
            btns[b].setBackground(Color.decode("#db9356"));
        }
    }
}
