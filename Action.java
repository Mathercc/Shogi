
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

//負責處理盤面上的動作，最複雜的部份
public class Action extends Map{
    
    //移動方法
    public static void move(JButton[] btns,StringBuilder map){
        for(int i=0;i<81;i++){
            JButton btn = btns[i];
            int clicked=i;
            btn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(e.getSource() == btn){
                        boolean move=false;
                        clear();
                        //如果點擊格子不是空格
                        if(btn.getText()!=""){
                            //判斷為哪方的棋子
                            if((turn%2==0&&btn.getForeground()==Color.WHITE)||
                               (turn%2==1&&btn.getForeground()==Color.BLACK)){
                                btn.setBackground(Color.decode("#e0bb79"));
                                //改變其可移動的格子的底色
                                for(int to=0;to<81;to++){
                                    if(Move.canMove(clicked,to,map.charAt(clicked))&&(btns[to].getForeground()!=btns[clicked].getForeground())){
                                        btns[to].setBackground(Color.decode("#ffb703"));
                                    }
                                }
                            }
                        }
                        //如果點擊的是空格
                        if(btn.getText()==""){
                            //判斷前一次點擊是否為可打入的持駒
                            if(lastClicked==81&&Move.canSet(clicked,temp)){
                                int number = Trans.ordinal(temp);
                                //若符合手番一致則將其打入
                                if(turn%2==0){
                                    map.setCharAt(clicked,Character.toUpperCase(temp));
                                    s[number]--;
                                    if(s[number]==0) senteHand[tempClicked].setVisible(false);
                                    else senteHand[number].setText(Trans.transPiece(temp,temp)+s[number]);
                                }
                                if(turn%2==1){
                                    map.setCharAt(clicked,Character.toLowerCase(temp));
                                    g[number]--;
                                    if(g[number]==0) goteHand[tempClicked].setVisible(false);
                                    else goteHand[number].setText(Trans.transPiece(temp,temp)+g[number]);
                                }
                                //更新盤面
                                setBoard(btns,map);
                                update(lastClicked,clicked,map.charAt(clicked),map.charAt(clicked));
                            }
                            //重置持駒
                            temp=' ';
                            tempClicked=81;
                        }
                        //判斷前一次點擊的棋子到這次點擊的格子是否符合其移動方式，若是則移動之
                        if(Move.canMove(lastClicked,clicked,map.charAt(lastClicked))&&(btns[lastClicked].getForeground())!=btns[clicked].getForeground()){
                            //判斷是否必須升級
                            char before=map.charAt(lastClicked);
                            if(Trans.mustPromote(before,clicked/9+1)) promote(map.charAt(lastClicked));
                            //否則判斷是否可以升級
                            else if(Trans.canPromote(before,lastClicked/9+1,clicked/9+1)){
                                clear();
                                int result = JOptionPane.showConfirmDialog(null,"是否升級?","升級",JOptionPane.YES_NO_OPTION);
                                if (result==JOptionPane.YES_OPTION)  promote(map.charAt(lastClicked));
                            }
                            //如果有吃到棋子則放入駒台
                            if(btns[clicked].getForeground()==Color.BLACK&&btns[lastClicked].getForeground()==Color.WHITE){
                                demote(map.charAt(clicked),clicked);
                                setHand(map.charAt(clicked));
                            }
                            if(btns[clicked].getForeground()==Color.WHITE&&btns[lastClicked].getForeground()==Color.BLACK){
                                demote(map.charAt(clicked),clicked);
                                setHand(map.charAt(clicked));
                            }
                            //更新盤面
                            map.setCharAt(clicked,map.charAt(lastClicked));
                            map.setCharAt(lastClicked,' ');
                            clear();
                            setBoard(btns,map);
                            update(lastClicked,clicked,before,map.charAt(clicked));
                            move=true;
                        }
                        //如果與前一次點擊相同的格子則清空選擇
                        if(lastClicked==clicked){
                            clear();
                            move=true;
                        }
                        //將本次點擊設為上次點擊
                        lastClicked=move?81:clicked;
                    }
                }
            });
        }
    }
    
    //打入方法
    public static void set(JButton[] hand,StringBuilder te){
        for(int i=0;i<7;i++){
            JButton btn=hand[i];
            int clicked=i;
            btn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(e.getSource() == btn){
                        //如果與前一次點擊相同的持駒則將清空選擇
                        boolean clear = temp==te.charAt(clicked);
                        //如果點擊的持駒可以打入，改變其可打入的格子的底色
                        if((turn%2==0)&&(btn.getForeground()==Color.WHITE)||(turn%2==1)&&(btn.getForeground()==Color.BLACK)){
                            clear();
                            tempClicked = clicked;
                            temp = te.charAt(clicked);
                            for(int to=0;to<81;to++){
                                if(Move.canSet(to,temp)){
                                    btns[to].setBackground(Color.decode("#ffb703"));
                                }
                            }
                        }
                        //如果與前一次點擊相同的持駒則清空選擇
                        if(clear){
                            clear();
                            temp=' ';
                            tempClicked=81;
                        }
                        //記錄上次點擊為持駒
                        lastClicked=81;
                    }
                }
            }); 
        }
    }
    
    //棋子穿梭敵陣時的升級
    public static void promote(char piece){
        switch(piece){
            case 'p': map.setCharAt(lastClicked,'t'); break;
            case 'P': map.setCharAt(lastClicked,'T'); break;
            case 'l': map.setCharAt(lastClicked,'a'); break;
            case 'L': map.setCharAt(lastClicked,'A'); break;
            case 'n': map.setCharAt(lastClicked,'j'); break;
            case 'N': map.setCharAt(lastClicked,'J'); break;
            case 's': map.setCharAt(lastClicked,'w'); break;
            case 'S': map.setCharAt(lastClicked,'W'); break;
            case 'b': map.setCharAt(lastClicked,'h'); break;
            case 'B': map.setCharAt(lastClicked,'H'); break;
            case 'r': map.setCharAt(lastClicked,'d'); break;
            case 'R': map.setCharAt(lastClicked,'D'); break;
        }
    }
    
    //吃到的棋子要先降級
    public static void demote(char piece,int clicked){
        switch(piece){
            case 't': map.setCharAt(clicked,'p'); break;
            case 'T': map.setCharAt(clicked,'P'); break;
            case 'a': map.setCharAt(clicked,'l'); break;
            case 'A': map.setCharAt(clicked,'L'); break;
            case 'j': map.setCharAt(clicked,'n'); break;
            case 'J': map.setCharAt(clicked,'N'); break;
            case 'w': map.setCharAt(clicked,'s'); break;
            case 'W': map.setCharAt(clicked,'S'); break;
            case 'h': map.setCharAt(clicked,'b'); break;
            case 'H': map.setCharAt(clicked,'B'); break;
            case 'd': map.setCharAt(clicked,'r'); break;
            case 'D': map.setCharAt(clicked,'R'); break;
        }
    }
}