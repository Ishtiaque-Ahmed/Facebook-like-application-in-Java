/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;
import java.io.*;
import java.net.*;
import java.util.*;
import static newserver.WorkerThread.login_verification;

/**
 *
 * @author ASUS
 */

public class NewServer {
  public static int workerThreadCount = 0;
  public static Vector<WorkerThread>threads=new Vector();
  public static Vector<String>users=new Vector();
  public static Vector<String>Online=new Vector();
  public static Vector<String>friends=new Vector();
  public static Vector<String>requests=new Vector();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
          int id = 1;
        try{
            FileInputStream fis=new FileInputStream("idpassword.txt");
                  BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                  String tem_str=br.readLine();
                  while(tem_str!=null)
                  {
                     
                     System.out.println(extract_username(tem_str));
                      users.add(extract_username(tem_str));
                      tem_str=br.readLine();
                  }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
            

        try {
            ServerSocket ss = new ServerSocket(5556);
            System.out.println("Server has been started successfully.");
            
            while (true) {
                Socket s = ss.accept();
                 BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                    String check=br.readLine();
                    try{
                    if(check.startsWith("log"))
                    {
                        System.out.println("login");
                         String tm=check.replace("log_","");
                         System.out.println(tm);
                            if(login_verification(tm))
                            {
                                    pr.println("ok");pr.flush();
                                    WorkerThread wt = new WorkerThread(s, extract_username(tm));
                                    threads.add(wt); 
                                    Thread t = new Thread(wt);
                                    t.start();
                             workerThreadCount++;
                                 System.out.println("Client [" + id + "] is now connected. No. of worker threads = " + workerThreadCount);
                            id++;
                             for(int i=0;i<users.size();i++)
                            {
                                pr.println("All_"+users.elementAt(i));pr.flush();
                                System.out.println("All_"+users.elementAt(i));
                             }
                             Helper helper=new Helper();
                             friends=helper.getFriends(extract_username(tm));
                             for(int i=0;i<friends.size();i++)
                             {
                                 pr.println("Friends_"+friends.elementAt(i));pr.flush();
                             }
                             requests=helper.getRequests(extract_username(tm));
                                for(int i=0;i<requests.size();i++)
                             {
                                 pr.println("Pending_"+requests.elementAt(i));pr.flush();
                             }
                                for(int i=0;i<NewServer.threads.size();i++)
                                {
                                    String LOL=NewServer.threads.elementAt(i).getid();
                                    String LEL=extract_username(tm);
                                    if(helper.isfriend(LOL,LEL))
                                    {
                                        System.out.println(LOL+" "+LEL);
                                        String LMAO="newonline_"+LEL;
                                        PrintWriter online_pr = new PrintWriter(NewServer.threads.elementAt(i).getstream());
                                        online_pr.println(LMAO);online_pr.flush();
                                        pr.println("newonline_"+LOL);pr.flush();
                                        
                                    }
                                    
                                }
                                Vector<String>lame=new Vector();
                                
                                lame=helper.get_message(extract_username(tm));
                                for(int i=0;i<lame.size();i++)
                                {
                                    System.out.println(lame.elementAt(i));
                                    pr.println(lame.elementAt(i));pr.flush();
                                }
                                
                               // pr.println("foul");pr.flush();
                            }
                            else 
                            {
                                pr.println("notok");pr.flush();
                            }
                    }
                    else 
                    {  
                        PrintWriter tpr = new PrintWriter(new BufferedWriter(new FileWriter("idpassword.txt", true)));
                        String tm=check.replace("sign_","");
                        tpr.println(tm);tpr.close();
                        pr.println("ok");pr.flush();
                        users.add(extract_username(tm));
                         WorkerThread wt = new WorkerThread(s, extract_username(tm));
                         threads.add(wt);
                            Thread t = new Thread(wt);
                             t.start();
                             workerThreadCount++;
                                 System.out.println("Client [" + id + "] is now connected. No. of worker threads = " + workerThreadCount);
                            id++;
                            for(int i=0;i<users.size();i++)
                            {
                                pr.println("All_"+users.elementAt(i));pr.flush();
                                System.out.println("All_"+users.elementAt(i));
                             }
                    }
                    }
                    catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                
       
            }
        } catch (Exception e) {
            System.err.println("Problem in ServerSocket operation. Exiting main.");
        }
    }
     public static String extract_username(String tem_str)
      {
           String final_tobeadded="";
                     for(int i=0;i<tem_str.length();i++)
                     {
                         if(tem_str.charAt(i)=='_')break;
                         final_tobeadded+=tem_str.charAt(i);
                     }
                     return final_tobeadded;
      }
    
    
    
    
      
}
        // TODO code application logic here
  class WorkerThread implements Runnable {

    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private String id = "";
    

    public WorkerThread(Socket s, String id) {
        this.socket = s;

        try {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
        } catch (Exception e) {
            System.err.println("Sorry. Cannot manage client [" + id + "] properly.");
        }

        this.id = id;
    }

    public String getid()
    {
        return id;
    }
    public OutputStream getstream()
    {
        return os;
    }
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
        PrintWriter pr = new PrintWriter(this.os);
        
     //   pr.println("Your id is: " + this.id);
       
     
     
        

        String str=null;
 
        while (true) {
            try {
                if (str!= null) {
                    System.out.println(str);
                  
                    if (str.equals("BYE")) {
                        System.out.println("[" + id + "] says: BYE. Worker thread will terminate now.");
                        break; // terminate the loop; it will terminate the thread also
                    } 
                    else if(str.startsWith("To"))
                    {
                        String [] parts=str.split("_");
                         Helper h=new Helper();
                        Vector<WorkerThread> temps=NewServer.threads;
                         if(h.isfriend(parts[1],this.getid()))
                         {
                             int flag=0;
                            for(int i=0;i<temps.size();i++)
                             {
                            if(temps.elementAt(i).id.equals(parts[1]))
                                 {
                                flag=1;
                                PrintWriter tem_pr=new PrintWriter(temps.elementAt(i).getstream());
                                tem_pr.println("msg_"+this.getid()+"_"+parts[2]);
                                System.out.println("msg_"+this.getid()+"_"+parts[2]);
                                tem_pr.flush();
                                break;
                                 }
                             }
                            if(flag==0)
                            {
                                String sender=parts[1]+"_"+parts[2]+"_"+this.getid();
                                h.off_msg(sender);
                            }
                         }
                            else
                            {
                                pr.println("Not_friend_");pr.flush();
                            }
                        }
                        
                    
                    else if(str.startsWith("req"))
                    {
                        String to=str.replace("req_","");
                        String current_name=this.getid();
                        System.out.println(current_name);
                        Helper helper=new Helper();
                        boolean status=helper.process_request(current_name,to);
                        System.out.println(status);
                        if(status)
                        {
                            pr.println("SuccessfulRequest");pr.flush();
                        }
                        else
                        {
                            pr.println("failed");pr.flush();
                        }
                    }
                    else if(str.startsWith("AcceptedRequest_"))
                    {
                        String from=str.replace("AcceptedRequest_", "");
                        String request_form=this.getid()+"_"+from;
                        Helper helper=new Helper();
                        helper.accept_request(request_form);
                        helper.add_to_friendlist(request_form);
                    }
                    else if(str.startsWith("SendFile_"))
                    {
                        String []parts=str.split("_");
                         try {
                    byte[] contents = new byte[10001];

                    FileOutputStream fos = new FileOutputStream(parts[1]);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    InputStream is = socket.getInputStream();

                    int bytesRead = 0;

                    while ((bytesRead = is.read(contents)) != -1) {
                        bos.write(contents, 0, bytesRead-1);
                        byte mark=contents[bytesRead-1];
                        if(mark==0)
                            break;
                        //System.out.println("bos is busy , mark = "+mark);
                    }

                    bos.flush();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    }
                    
                    else if (str.startsWith("DL")) {
                        
                         int flag=0; String []parts=str.split("_");
                    
                        try {
                           
                            
                             Vector<WorkerThread>temps=new Vector();
                            for(int i=0;i<temps.size();i++)
                             {
                            if(temps.elementAt(i).id.equals(parts[1]))
                                 {
                                flag=1;
                                break;
                                 }
                             }
                            if(flag==0)
                            {
                                String line=parts[1]+"_"+parts[2];
                                try{
                                    PrintWriter epr = new PrintWriter(new BufferedWriter(new FileWriter("offlinefile.txt", true)));
                                     epr.println(line);
                                     epr.close();
            
                                }
                                catch(Exception e)
                                 {
                                         e.printStackTrace();
                                      }
                            }
                         }catch(Exception e)
                         {
                             e.printStackTrace();
                         }
                        if(flag==1)
                        {
                            try{
                            File file = new File(parts[2]);
                            FileInputStream fis = new FileInputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            OutputStream os = socket.getOutputStream();
                            byte[] contents;
                            long fileLength = file.length();
                            long current = 0;

                            long start = System.nanoTime();
                            delay(100000);
                            while (current != fileLength) {
                                int size = 10000;
                                byte mark;
                                if (fileLength - current > size) {
                                    current += size;
                                    mark=1;
                                } else {
                                    size = (int) (fileLength - current);
                                    current = fileLength;
                                    mark=0;
                                }
                                contents = new byte[size+1];
                                bis.read(contents, 0, size);
                                contents[size]=mark;
                                os.write(contents);
                                System.out.println("Sending file ... " + (current * 100) / fileLength + "% complete!");
                            }

                            os.flush();
                            System.out.println("File sent successfully!");
                            delay(100000);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Could not transfer file.");
                        }
                        pr.println("Downloaded.");
                        pr.flush();
                        }
                    
                    } else {
                        System.out.println("[" + id + "] says: " + str);
                        pr.println("Got it. You sent \"" + str + "\"");
                        pr.flush();
                    }
                } /*else {
                    System.out.println("[" + id + "] terminated connection. Worker thread will terminate now.");
                    break;
                }*/
                str=br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Problem in communicating with the client [" + id + "]. Terminating worker thread.");
                break;
            }
        }
            
        try {
            this.is.close();
            this.os.close();
            this.socket.close();
        } catch (Exception e) {
                System.out.println("sick of this");
        }
          for(int i=0;i<NewServer.threads.size();i++)
          {
              if(NewServer.threads.elementAt(i).getid().equals(id))
                  NewServer.threads.remove(i);
          }
          
               for(int i=0;i<NewServer.threads.size();i++)
          {
              PrintWriter tempar=new PrintWriter(NewServer.threads.elementAt(i).getstream());
              tempar.println("leaving_"+id);tempar.flush();
          }
       NewServer.workerThreadCount--;
        System.out.println("Client [" + id + "] is now terminating. No. of worker threads = "
                + NewServer.workerThreadCount);
    }

    private void delay(int p) {
        int k=0;
        while(k<p)
            k++;
    }
      public static boolean login_verification(String s)
        {
            try{
       
                  FileInputStream fis=new FileInputStream("idpassword.txt");
                  BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                  String str=br.readLine();
        while(str!=null)
        {
            
            if(s.equals(str))return true;
            str=br.readLine();
        }
   
        }catch(Exception e)
        {
            System.out.println("Verification problem");
        }
            return false;
        }
     
}

