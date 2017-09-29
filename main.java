import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by David on 24/01/2017.
 */
public class main implements ActionListener, ListSelectionListener{
    private static DefaultListModel emailListModel;
    private static DefaultListModel keyListModel;
    private static DefaultListModel unsubscribeListModel;
    private static ArrayList emialListArray;
    private static ArrayList keyListArray;
    private static ArrayList unsubscribeListArray;

    public static void main(String[] args){
        emailListModel = new DefaultListModel();
        keyListModel = new DefaultListModel();
        unsubscribeListModel = new DefaultListModel();
        emialListArray = new ArrayList();
        keyListArray = new ArrayList();
        unsubscribeListArray = new ArrayList();


        String temp = "";
        try{
            File emailFile = new File(System.getProperty("user.dir") + "\\src\\email.txt");
            File keyFile = new File(System.getProperty("user.dir") + "\\src\\key.txt");
            BufferedReader br = new BufferedReader(new FileReader(emailFile));
            do
            {
                temp = br.readLine();
                System.out.println(temp);
                if(temp != null) {
                    emailListModel.addElement(temp);
                }
            }while(temp != null);
            br = new BufferedReader(new FileReader(keyFile));
            do
            {
                temp = br.readLine();
                System.out.println(temp);
                if(temp != null) {
                    keyListModel.addElement(temp);
                }
            }while(temp != null);
        }catch(Exception ee)
        {
            ee.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("POSTMARK APP");

        //Create and set up the content pane.
        main demo = new main();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }


    private JTextField sender;
    private JTextField recipient;
    private JTextField subject;
    private JTextArea body;
    private JTextField newEmail;
    private JButton send;
    private JButton cancel;
    private JButton loadEmailList;
    private JButton subscriberList;
    private JButton add;
    private JButton remove;
    private JList emailList;


    private JPanel emailPanel[] = new JPanel[9];
    private JLabel label[] = new JLabel[8];

    private int panelLocation = 25;

    private String senderStr = "";
    private String recipientStr = "";
    private String subjectStr = "";
    private String bodyStr = "";

    private boolean flagSubscriber = true;

    public JPanel createContentPane(){

        // We create a bottom JPanel to place everything on.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);


        //******************************** SENDER *********************************************

        emailPanel[0] = new JPanel();
        emailPanel[0].setLayout(null);
        emailPanel[0].setLocation(100, panelLocation*4);
        emailPanel[0].setSize(610, panelLocation);
        totalGUI.add(emailPanel[0]);

        emailPanel[8] = new JPanel();
        emailPanel[8].setLayout(null);
        emailPanel[8].setLocation(700, panelLocation * 4);
        emailPanel[8].setSize(25, panelLocation);
        emailPanel[8].setVisible(false);
        totalGUI.add(emailPanel[8]);


        label[0] = new JLabel("Sender");
        label[0].setLocation(0, 0);
        label[0].setSize(100, 25);
        emailPanel[0].add(label[0]);

        sender = new JTextField("G00313010@gmit.ie");
        sender.setLayout(null);
        sender.setEditable(false);
        sender.setLocation(100 ,0);
        sender.setSize(600, 25);
        emailPanel[0].add(sender);

        newEmail = new JTextField("");
        newEmail.setLayout(null);
        newEmail.setLocation(100, 0);
        newEmail.setSize(600, 25);
        newEmail.setVisible(false);
        emailPanel[0].add(newEmail);


        //******************************** RECIPIENT ******************************************

        emailPanel[1] = new JPanel();
        emailPanel[1].setLayout(null);
        emailPanel[1].setLocation(100, panelLocation*5);
        emailPanel[1].setSize(610, panelLocation);
        totalGUI.add(emailPanel[1]);

        label[1] = new JLabel("Recipient");
        label[1].setLocation(0, 0);
        label[1].setSize(100, 25);
        emailPanel[1].add(label[1]);

        loadEmailList= new JButton("Load Mailing List");
        loadEmailList.setLocation(410, 0);
        loadEmailList.setSize(200, panelLocation);
        loadEmailList.addActionListener(this);
        emailPanel[1].add(loadEmailList);

        recipient = new JTextField();
        recipient.setLayout(null);
        recipient.setEditable(true);
        recipient.setLocation(100 ,0);
        recipient.setSize(300, 25);
        emailPanel[1].add(recipient);

        //******************************** ADD EMAIL ******************************************

        add = new JButton("ADD");
        add.setLocation(100, 0);
        add.setSize(250, panelLocation);
        add.addActionListener(this);
        add.setVisible(false);
        emailPanel[1].add(add);


        //******************************** SUBJECT ********************************************

        emailPanel[2] = new JPanel();
        emailPanel[2].setLayout(null);
        emailPanel[2].setLocation(100, panelLocation*6);
        emailPanel[2].setSize(610, panelLocation);
        totalGUI.add(emailPanel[2]);

        label[2] = new JLabel("Subject");
        label[2].setLocation(0, 0);
        label[2].setSize(100, 25);
        emailPanel[2].add(label[2]);

        subject = new JTextField();
        subject.setLayout(null);
        subject.setEditable(true);
        subject.setLocation(100 ,0);
        subject.setSize(600, 25);
        emailPanel[2].add(subject);

        label[4] = new JLabel("Error:");
        label[4].setLocation(0, 0);
        label[4].setSize(100, panelLocation);
        label[4].setVisible(false);
        emailPanel[2].add(label[4]);

        label[5] = new JLabel("");
        label[5].setLocation(100, 0);
        label[5].setSize(500, panelLocation);
        label[5].setVisible(false);
        emailPanel[2].add(label[5]);

        //******************************** EMAILER LIST ********************************************

        emailPanel[6] = new JPanel();
        emailPanel[6].setLayout(null);
        emailPanel[6].setLocation(200, panelLocation * 8);
        emailPanel[6].setSize(610, panelLocation * 10);
        emailPanel[6].setVisible(false);
        totalGUI.add(emailPanel[6]);

        emailList = new JList(emailListModel);
        emailList.setLayout(null);
        emailList.setLocation(0, 0);
        emailList.setSize(500, 200);
        emailList.setVisibleRowCount(10);
        emailList.setFixedCellHeight(20);
        emailList.setFixedCellWidth(140);
        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.addListSelectionListener(this);
        emailPanel[6].add(emailList);


        //******************************** BODY ********************************************

        emailPanel[3] = new JPanel();
        emailPanel[3].setLayout(null);
        emailPanel[3].setLocation(100, panelLocation * 7);
        emailPanel[3].setSize(610, panelLocation * 6);
        totalGUI.add(emailPanel[3]);

        label[3] = new JLabel("Body");
        label[3].setLocation(0, 0);
        label[3].setSize(100, 25);
        emailPanel[3].add(label[3]);

        body = new JTextArea();
        body.setLayout(null);
        body.setEditable(true);
        body.setLineWrap(true);
        body.setLocation(100, 0);
        body.setSize(600, panelLocation * 6);
        emailPanel[3].add(body);

        //******************************** REMOVE BTN *************************************************


        emailPanel[7] = new JPanel();
        emailPanel[7].setLayout(null);
        emailPanel[7].setLocation(100, panelLocation * 16);
        emailPanel[7].setSize(610, panelLocation);
        emailPanel[7].setVisible(false);
        totalGUI.add(emailPanel[7]);

        remove = new JButton("REMOVE");
        remove.setLocation(100, 0);
        remove.setSize(250, panelLocation);
        remove.addActionListener(this);
        emailPanel[7].add(remove);


        //******************************** SEND/CANCEL BTN ********************************************

        emailPanel[4] = new JPanel();
        emailPanel[4].setLayout(null);
        emailPanel[4].setLocation(100, panelLocation * 14);
        emailPanel[4].setSize(610, panelLocation);
        totalGUI.add(emailPanel[4]);

        send = new JButton("SEND");
        send.setLocation(100, 0);
        send.setSize(250, panelLocation);
        send.addActionListener(this);
        emailPanel[4].add(send);

        cancel = new JButton("CANCEL");
        cancel.setLocation(350, 0);
        cancel.setSize(250, panelLocation);
        cancel.addActionListener(this);
        emailPanel[4].add(cancel);

        //******************************** Switch Button ********************************************

        emailPanel[5] = new JPanel();
        emailPanel[5].setLayout(null);
        emailPanel[5].setLocation(100, panelLocation * 18);
        emailPanel[5].setSize(610, panelLocation);
        totalGUI.add(emailPanel[5]);

        subscriberList = new JButton("Subscriber List");
        subscriberList.setLocation(100, 0);
        subscriberList.setSize(250, panelLocation);
        subscriberList.addActionListener(this);
        emailPanel[5].add(subscriberList);

        totalGUI.setOpaque(true);
        return totalGUI;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            senderStr = sender.getText();
            recipientStr = recipient.getText();
            subjectStr = subject.getText();
            bodyStr = body.getText();
            sendPostmark(senderStr, recipientStr, subjectStr, bodyStr);
        } else if (e.getSource() == cancel) {
            recipient.setText("");
            subject.setText("");
            body.setText("");
        } else if (e.getSource() == remove) {
            System.out.println(emailListModel.size());
            keyListModel.removeElementAt(emailList.getSelectedIndex());
            emailListModel.removeElementAt(emailList.getSelectedIndex());
            sortEmails();
            updateMailingList();
        } else if (e.getSource() == loadEmailList) {
            recipient.setText(loadMailingList());
        } else if (e.getSource() == add) {
            if (checkEmail(newEmail.getText())) {
                emailListModel.addElement(newEmail.getText());
                String key = "";
                Random rand = new Random(System.currentTimeMillis());
                for(int i = 0; i < 10; i++)
                {
                    key += (char)(rand.nextInt(26)+65);
                }
                System.out.println("Key = " + key);
                keyListModel.addElement(key);
                newEmail.setText("");
                newEmail.setBackground(Color.white);
                sortEmails();
                updateMailingList();
            } else {
                newEmail.setBackground(Color.red);
            }
        } else if (e.getSource() == subscriberList) {
            flagSubscriber = !flagSubscriber;

            // Email GUI
            sender.setVisible(flagSubscriber);
            recipient.setVisible(flagSubscriber);
            subject.setVisible(flagSubscriber);
            body.setVisible(flagSubscriber);
            loadEmailList.setVisible(flagSubscriber);
            send.setVisible(flagSubscriber);
            cancel.setVisible(flagSubscriber);
            for (int i = 0; i < 4; i++) {
                label[i].setVisible(flagSubscriber);
            }
            //historyList.setVisible(flagSubscriber);

            newEmail.setVisible(!flagSubscriber);
            add.setVisible(!flagSubscriber);
            label[4].setVisible(!flagSubscriber);
            label[5].setVisible(!flagSubscriber);
            emailPanel[6].setVisible(!flagSubscriber);
            emailPanel[7].setVisible(!flagSubscriber);

            if (flagSubscriber) {
                label[0].setText("Sender: ");
                label[0].setVisible(true);
                label[3].setText("Body: ");
                label[3].setVisible(true);
                subscriberList.setText("Subscriber List");
                checkUnsubscribes();

            } else {
                label[0].setText("New Subscriber: ");
                label[0].setVisible(true);
                label[3].setText("Email List: ");
                label[3].setVisible(true);
                subscriberList.setText("Send an Email");
            }

        }
    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting() == false) {
            if (e.getSource() == emailList) {
                for(int i = 0; i < emailListModel.size(); i++)
                {
                    if (i == emailList.getSelectedIndex())
                    {
                        System.out.println("Selection is " + emailListModel.elementAt(i));
                    }
                }
            }
        }
    }

    public String loadMailingList()
    {
        File f = new File(System.getProperty("user.dir") + "\\src\\email.txt");
        String addresses = "";
        String temp = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            do
            {
                temp = br.readLine();
                if(temp != null)
                {
                    addresses+=temp;
                    addresses+="~";
                }
            }while(temp != null);
            addresses = addresses.replaceAll("~", " ");
            addresses = addresses.trim();
            addresses = addresses.replaceAll(" ", ",");
        }catch(Exception ee)
        {
            ee.printStackTrace();
        }
        return addresses;
    }

    public void updateMailingList()
    {
        File emailFile = new File(System.getProperty("user.dir") + "\\src\\email.txt");
        File keyFile = new File(System.getProperty("user.dir") + "\\src\\key.txt");
        File unsubscribeFile = new File(System.getProperty("user.dir") + "\\src\\unsubscribe.txt");
        try{
            if(emailFile.exists()) {
                PrintWriter pw = new PrintWriter(new FileWriter(emailFile));
                for (int i = 0; i < emailListModel.size(); i++) {
                    pw.println(emailListModel.elementAt(i).toString());
                }
                pw.flush();
                pw.close();
            }
            if(keyFile.exists()) {
                PrintWriter pw = new PrintWriter(new FileWriter(keyFile));
                for (int i = 0; i < keyListModel.size(); i++) {
                    pw.println(keyListModel.elementAt(i).toString());
                }
                pw.flush();
                pw.close();
            }
            if(unsubscribeFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(unsubscribeFile));
                String temp = "";
                String checkEmails = "";
                do
                {
                    temp = br.readLine();
                    System.out.println(temp);
                    if(temp != null) {
                        checkEmails += temp;
                    }
                }while(temp != null);
                br.close();
                System.out.println("Unsub: " + checkEmails);
                boolean print = true;
                for (int i = 0; i < unsubscribeListModel.size(); i++) {
                    if(checkEmails.contains(unsubscribeListModel.elementAt(i) + ""))
                    {
                        System.out.println("Email is already on subscriberlist");
                    }
                    else {
                        PrintWriter pw = new PrintWriter(new FileWriter(unsubscribeFile), true);
                        pw.println("" + unsubscribeListModel.elementAt(i));
                        pw.flush();
                        pw.close();
                    }
                }

            }
        }catch(Exception ee)
        {
            ee.printStackTrace();
        }
    }
    public boolean checkEmail(String emailQuery)
    {
        boolean status = true;
        if(!isValidEmailAddress(emailQuery))
        {
            System.out.println("NOT VALID EMAIL");
            status = false;
        }

        for(int i = 0; i < emailListModel.size(); i++)
        {
            if(isSameEmailAddress(emailQuery, emailListModel.elementAt(i).toString()))
            {
                System.out.println("EMAIL ALREADY EXISTS");
                status = false;
            }
        }
        return status;
    }

    public boolean isSameEmailAddress(String email1, String email2){    // Check to see if two emails are the same, regardless of Case or whitespace
        if(email1.trim().compareToIgnoreCase(email2.trim()) == 0){      // Trim both strings, and compare both of them regardless of their case
            return true;        // If they are the same, Pass the test
        }
        else {
            return false;       // If they are different, Fail the test
        }
    }

    public boolean isValidEmailAddress(String emailAddress){            // check to see if an email is valid

        // Check if there is leading or trailing whitespace characters at the very start or very end of the email address
        if(Character.isWhitespace(emailAddress.charAt(0))){
            label[5].setText("Whitespace inserted at beginning of email Address !");
            return false;       // If there is white space, fail it
        }
        else if(Character.isWhitespace(emailAddress.charAt(emailAddress.length()-1)))
        {
            label[5].setText("Whitespace inserted at beginning of email Address !");
            return false;       // If there is white space, fail it
        }
        // Emails need a '.', check if there is one presnt in the emailAddress String. (indexOf returns -1 if a character is not found in a string)
        else if(emailAddress.indexOf('.') == -1 && emailAddress.lastIndexOf('.') > emailAddress.indexOf("@")){
            label[5].setText("No '.' (dot) found in address after @ symbol !");
            return false;       // If there is no '.' then fail it
        }
        // Emails cant start with a '.' or end with a '.', This checks to see if there is
        else if(emailAddress.charAt(0) == '.' || emailAddress.charAt(emailAddress.length() - 1) == '.'){
            label[5].setText("'.' (dot) cannot be the start or end of an email address !");
            return false;       // if there is a '.' at the very start or end, fail it
        }
        // Emails need a '@', check if there is one presnt in the emailAddress String. (indexOf returns -1 if a character is not found in a string)
        else if(emailAddress.indexOf('@') == -1){
            label[5].setText("No '@' found in address !");
            return false;    // If there is no '@' then fail it
        }
        // Emails cant start with a '@' or end with a '@', This checks to see if there is
        else if (emailAddress.charAt(0) == '@' || emailAddress.charAt(emailAddress.length()-1) == '@'){
            label[5].setText("'@' cannot be the start or end of an email address !");
            return false;    // If there is no '@' then fail it
        }
        else if(emailAddress.indexOf("@") > emailAddress.lastIndexOf(".")-2){
            label[5].setText("No email domain found (sample@????.XXX !");
            return false;    // If there is no '@' then fail it
        }
        else if(emailAddress.contains(" ")){
            label[5].setText("No whitespace can be present in the email address");
            return false;    // If there is no '@' then fail it
        }
        else{       // If all this criteria is OK then pass it
            System.out.println(emailAddress);    // prints the valid emails
            return true;   // Its vailid, Pass it
        }
    }

    public void sortEmails() {
        emialListArray.clear();
        for(int i = 0; i < emailListModel.size(); i++)
        {
            emialListArray.add(emailListModel.elementAt(i));
        }
        Collections.sort(emialListArray);
        emailListModel.clear();
        for(int i = 0; i < emialListArray.size(); i++)
        {
            emailListModel.addElement((String) emialListArray.get(i));
        }
    }

    private static void sendPostmark(String fromAddr, String toAddr, String subject, String body){

        String temp = "";
        String header = "";
        String toAddrsTemp[] = toAddr.split(",");


        DefaultListModel toAddrsList = new DefaultListModel();
        unsubscribeListModel = new DefaultListModel();
        for (int i = 0; i < toAddrsTemp.length; i++) {
            toAddrsList.addElement(toAddrsTemp[i]);
        }
        try{

            File f = new File(System.getProperty("user.dir") + "\\src\\unsubscribe.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            int i = 0;
            do{
                temp = br.readLine();
                if(temp != null)
                {
                    unsubscribeListModel.addElement(temp);
                    System.out.println("Unsubscribe:" + unsubscribeListModel.elementAt(i++));
                }
            }while(temp != null);
            br.close();
        }catch(Exception ee) {
            ee.printStackTrace();
        }
        System.out.println("AddrList: " + toAddrsList.size());
        System.out.println("UnsubList: " + unsubscribeListModel.size());
        System.out.println("Size: " + (toAddrsList.size() - unsubscribeListModel.size()));
        String toAddrs[] = new String[(toAddrsList.size() - unsubscribeListModel.size())];
        int index = 0;
        for (int i = 0; i < toAddrsList.size(); i++)
        {
            int count = 0;
            for (int j = 0; j < unsubscribeListModel.size(); j++) {
                if(!toAddrsList.elementAt(i).toString().equals(unsubscribeListModel.elementAt(j).toString()))
                {
                    count++;
                    if(count == unsubscribeListModel.size())
                    {
                        //System.out.println("To Addresses:" + unsubscribeListModel.elementAt(i));
                        toAddrs[index++] = toAddrsList.elementAt(i).toString();
                    }
                }
            }
        }
        try{
            Socket sc = new Socket("api.postmarkapp.com", 80);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            for(int i = 0; i < toAddrs.length; i++)
            {
                System.out.println(toAddrsList.elementAt(i));
            }
            for(int i = 0; i < toAddrs.length; i++) {
                    String msg = "{From: '" + fromAddr + "'," +
                            " To: '" + toAddrsList.elementAt(i) + "', " +
                            "ReplyTo: 'e88e2ded54b7d5031fe123b17c26050e@inbound.postmarkapp.com', " +
                            "Subject: '" + subject + "', " +
                            "TextBody: '" + body + "\r\n\r\n\r\nTo unsubscribe, reply with the message - UNS~(key)\r\nYour key is : " + keyListModel.elementAt(i) + "'}";
                    System.out.println(msg);
                    pw.println("POST http://api.postmarkapp.com/email HTTP/1.1");
                    pw.println("Host: localhost");
                    pw.println("Accept: application/json");
                    pw.println("Content-Type: application/json");
                    pw.println("X-Postmark-Server-Token: XXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    pw.println("Content-Length:" + msg.length());
                    pw.println();
                    pw.print(msg);
                    pw.flush();
                    do {
                        temp = br.readLine();   // Read a line
                        header += temp;// Add the newly read line to the request
                        header += "~\r\n";
                    } while (!temp.isEmpty());  // while there is data to read, continue reading in the next line
                    int contentLength = Integer.parseInt(header.substring(header.indexOf("Content-Length: ") + "Content-Length: ".length(), header.indexOf("~", header.indexOf("Content-Length: "))));
                    char responseCh[] = new char[contentLength];
                    br.read(responseCh, 0, contentLength);
                    String response = new String(responseCh);
                    if(!response.contains("ErrorCode\":0")){
                        System.out.println("CRITICAL ERROR");
                        System.out.println(response);
                        System.out.println("Iteration No. " + i);
                        break;
                    }else
                    {
                        System.out.println("SUCCESS");
                        System.out.println(response);
                        long currentMillis = System.currentTimeMillis();
                        while(currentMillis+5000 > System.currentTimeMillis());
                    }
                }
            pw.close();
            br.close();
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
    }

    public void checkUnsubscribes()
    {
        String temp = "";
        String header = "";
        String findStr = "StrippedTextReply:";
        int lastIndex = 0;
        int count = 0;
        int indexes[] = new int[200];
        unsubscribeListModel = new DefaultListModel();
        try{

            URL ur = new URL("http://requestb.in/w9dndww9?inspect");
            URLConnection conn = ur.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            File f = new File(System.getProperty("user.dir") + "\\src\\temp.txt");
            System.out.println(f);
            PrintWriter pw = new PrintWriter(new FileWriter(f));

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            do {
                temp = br.readLine();   // Read a line
                header += temp;
                header += "\r\n";
            } while (temp != null);  // while there is data to read, continue reading in the next line
            //System.out.println(header);
            header = header.replaceAll("&#34;", "");
            pw.print(header);
            pw.flush();
            pw.close();
            //br.close();
            //br = new BufferedReader(new FileReader(f));
            //do{
             //   temp += br.readLine();   // Read a line
            //}while(temp != null);
            br.close();



            while(lastIndex != -1){

                lastIndex = header.indexOf(findStr,lastIndex);

                if(lastIndex != -1){
                    lastIndex += findStr.length();
                    indexes[count] = lastIndex;
                    System.out.println("indexes"+ count + " = " + indexes[count]);
                    count ++;
                }
            }
            System.out.println("indexes"+ count + " = " + indexes[0]);
            for(int i = 0; i < indexes.length; i++)
            {
                System.out.println(indexes[i]);
                int startIndex = indexes[i];
                System.out.println("Start Index: " + startIndex);
                int endIndex = header.indexOf(",", startIndex);
                System.out.println("End Index: " + endIndex);
                String str = header.substring(startIndex, endIndex);
                //System.out.println(str);
                String keyStr = "";
                if(str.contains("UNS~"))
                {
                    int subStartIndex = str.indexOf("UNS~") + "UNS~".length();
                    int subEndIndex = subStartIndex+10;
                    System.out.println("1: " + subStartIndex);
                    System.out.println("2: " + subEndIndex);
                    keyStr = str.substring(subStartIndex, subEndIndex);
                }
                System.out.println("KEY: " + keyStr);
                for(int j = 0; j < emailListModel.size(); j++)
                {
                    System.out.println(keyListModel.elementAt(j) + " == " + keyStr);
                    if(keyListModel.elementAt(j).toString().trim().equals(keyStr))
                    {
                        System.out.println("MATCH");
                        unsubscribeListModel.addElement(emailListModel.elementAt(j));
                        //emailListModel.removeElementAt(j);
                        //keyListModel.removeElementAt(j);
                    }
                }
            }
            updateMailingList();
        }catch(Exception ee){
            ee.printStackTrace();
        }
    }
}
