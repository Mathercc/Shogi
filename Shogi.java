
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

//遊戲主視窗程式，負責處理視窗介面。
public class Shogi extends JFrame{

    //棋盤的格子資訊
    static StringBuilder map = new StringBuilder("lnsgkgsnl"
                                                +" r     b "
                                                +"ppppppppp"
                                                +"         "
                                                +"         "
                                                +"         "
                                                +"PPPPPPPPP"
                                                +" B     R "
                                                +"LNSGKGSNL ");
    
    //先手(sente) 後手(gote) 持駒資訊
    static StringBuilder sen = new StringBuilder("plnsgbr");
    static StringBuilder go = new StringBuilder("PLNSGBR");
    
    //持駒數量
    static int[] s = {0,0,0,0,0,0,0};
                    //P,L,N,S,G,B,R
    static int[] g = {0,0,0,0,0,0,0};

    //遊戲視窗
    static JFrame jf = new JFrame("視窗");
    //棋盤81個格子
    static JButton[] btns = new JButton[81];
    //先手、後手駒台
    static JButton[] senteHand = new JButton[7];
    static JButton[] goteHand = new JButton[7];
    static JPanel sente = new JPanel(new GridLayout(0,8));
    static JPanel gote = new JPanel(new GridLayout(0,8));
    //棋局資訊(手數、手番、上一步)
    static JLabel steps = new JLabel("手數：0  ");
    static JLabel turns = new JLabel("先手番 ");
    static JLabel moves = new JLabel();
    //上一個點擊的持駒
    static char temp = ' ';
    static int tempClicked = 81;
    //上一個點擊的棋盤格子，0~80為盤面對應格子；81為持駒
    static int lastClicked = 81;
    //手數
    static int turn = 0;
    
    //建立新遊戲
    public Shogi(){
        //建立棋盤
        JPanel info = new JPanel();
        JPanel board = new JPanel(new GridLayout(9,9));
        board.setSize(500,500);
        steps.setFont(new Font("標楷體", Font.PLAIN, 20));
        turns.setFont(new Font("標楷體", Font.PLAIN, 20));
        moves.setFont(new Font("標楷體", Font.PLAIN, 20));
        gote.setPreferredSize(new Dimension(500,50));
        sente.setPreferredSize(new Dimension(500,50));
        info.setPreferredSize(new Dimension(150,0));
        info.add(steps);
        info.add(turns);
        info.add(moves);
        //建立先手駒台
        for(int i=0;i<7;i++){
            senteHand[i]=new JButton();
            senteHand[i].setBackground(Color.decode("#db9356"));
            senteHand[i].setForeground(Color.WHITE);
            senteHand[i].setFont(new Font("標楷體", Font.PLAIN, 20));
            senteHand[i].setVisible(false);
            sente.add(senteHand[i]);
        }
        //建立後手駒台
        for(int i=0;i<7;i++){
            goteHand[i]=new JButton();
            goteHand[i].setBackground(Color.decode("#db9356"));
            goteHand[i].setForeground(Color.BLACK);
            goteHand[i].setFont(new Font("標楷體", Font.PLAIN, 20));
            goteHand[i].setVisible(false);
            gote.add(goteHand[i]);
        }
        //建立棋盤格子
        for(int i=0;i<81;i++){
            btns[i] = new JButton();
            btns[i].setBackground(Color.decode("#db9356"));
            btns[i].setBorder(new LineBorder(Color.BLACK));
            board.add(btns[i]);
        }
        //建立說明按鈕
        JButton help = new JButton("？");
        help.setBackground(Color.decode("#cccccc"));
        help.setFont(new Font("標楷體", Font.BOLD, 40));
        help.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                    JOptionPane.showMessageDialog(jf,
                      "<html><p style='width: 450px; font-size: 10px;'>"
                    + "遊戲進行：<br>"
                    + "一局將棋由先手開始下一手，接著輪到後手下一手，如此往復。直到其中一方被將死。<br>"
                    + "每一手有兩種選擇：<br>"
                    + "1.移動棋子：將自己棋盤上一枚棋子按照其移動方式移動到一個不是自己棋子所在的格子。<br>"
                    + "2.打入棋子：將自己駒台上一枚棋子符合打入規則放入棋盤中一個沒有棋子的格子。<br>"
                    + "但如果自己的玉將正處於王手狀態，則行動必須同時可以解除王手。"
                    + "如果將自己的棋子移動到對手棋子所在的格子，則會將原來位置的棋子放置於自己身旁的駒台，成為持駒。"
                    + "而當一枚已升級的棋子被放入駒台時會先恢復未升級的狀態再放入。"
                    + "一名玩家下完一手後，如果在下一手可以吃掉對方的玉將，則稱為王手；"
                    + "被王手的一方必須在下一手設法解除王手狀態，無法解除則被將死。<br><br>"
                    + "移動方式：<br>"
                    + "玉將(玉)：直向一格、斜向一格；無法升級。<br>"
                    + "飛車(飛)：直向任意格，不可越子；升級後為龍王(龍)，除了繼承原移動方式外增加了斜向一格。<br>"
                    + "角行(角)：斜向任意格，不可越子；升級後為龍馬(馬)，除了繼承原移動方式外增加了直向一格。<br>"
                    + "金將(金)：斜前方一格、直向一格；無法升級。<br>"
                    + "銀將(銀)：正前方一格、斜向一格；升級後為成銀(全)，移動方式與金將相同。<br>"
                    + "桂馬(桂)：正前方兩格的左或右一格，可越子；升級後為成桂(圭)，移動方式與金將相同。<br>"
                    + "香車(香)：正前方任意格，不可越子；升級後為成香(杏)，移動方式與金將相同。<br>"
                    + "步兵(步)：正前方一格；升級後為成金(と)，移動方式與金將相同。<br><br>"
                    + "升級條件：<br>"
                    + "除了玉將、金將外的其它六種棋子均可升級。當一枚未升級且可以升級的棋子移動後，"
                    + "如果移動前、移動後之中至少有一個位於敵陣(後三排)，可以選擇是否將棋子升級並立刻改變走法，之後的遊戲過程中不能主動將升級的棋子降級。"
                    + "若維持原本的走法無法再移動則會強制升級。打入時即使打入在敵陣中也不能立刻升級， 一定要移動過且符合條件才可以升級。<br></p></html>",
			"遊戲規則",JOptionPane.DEFAULT_OPTION);
            }
        });
        gote.add(help);
        //建立作者資訊按鈕
        JButton auth = new JButton("i");
        auth.setBackground(Color.decode("#cccccc"));
        auth.setFont(new Font("標楷體", Font.BOLD, 40));
        auth.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                    JOptionPane.showMessageDialog(jf,
					"<html><p style='width: 450px; font-size: 20px;'>"
                + "作者：江浩潁<br>"
                + "國立臺北教育大學 數二甲 遊戲程式設計<br>"
                + "關於更多詳細的將棋資訊請至<br>"
                + "https://sites.google.com/view/shogi110813032/</p></html>",
					"關於作者",
						JOptionPane.DEFAULT_OPTION);
            }
        });
        sente.add(auth);
        //視窗設定
        jf.setLayout(new BorderLayout());
        jf.setBounds(350,50,650,600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setTitle("將棋");
        jf.add(board,BorderLayout.CENTER);
        jf.add(info,BorderLayout.LINE_END);
        jf.add(gote,BorderLayout.PAGE_START);
        jf.add(sente,BorderLayout.PAGE_END);
        jf.setVisible(true);
        Map.setBoard(btns,map);
        Action.move(btns,map);
        Action.set(senteHand,sen);
        Action.set(goteHand,go);
    }
    
    //更新遊戲
    public static void update(int from,int to,char before,char after){
        turn++;
        //回合數增加
        steps.setText("手數："+turn);
        //顯示上一手的內容
        if(turn%2==0){
            turns.setText(" 先手番 ");
            moves.setText("▽ "+(9-to%9)+Trans.transNum(to/9+1)+Trans.transPiece(before,after)+Trans.utsu(from));
        }
        else{
            turns.setText(" 後手番 ");
            moves.setText("▲ "+(9-to%9)+Trans.transNum(to/9+1)+Trans.transPiece(before,after)+Trans.utsu(from));
        }
        //判斷遊戲勝負
        if(Map.checkmate(map)){
            if(turn%2==0){
                JOptionPane.showMessageDialog(null,"手數："+turn+"\n"+"後手勝","遊戲結束",JOptionPane.INFORMATION_MESSAGE);
            }
            if(turn%2==1){
                JOptionPane.showMessageDialog(null,"手數："+turn+"\n"+"先手勝","遊戲結束",JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }
    }
}