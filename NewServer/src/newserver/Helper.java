/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;
import java.util.*;
import java.io.*;
/**
 *
 * @author ASUS
 */
public class Helper {
    
    public static Vector<String> getFriends(String name)
    {
        Vector<String>Oregami=new Vector();
        try{
             FileInputStream fis=new FileInputStream("friendlist.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
        
        String str=br.readLine();
        while(str!=null)
        {
            
           String [] parts=str.split("_");
           //System.out.println("From helper "+parts[1]);
           if(parts[0].equals(name))Oregami.add(parts[1]);
           else if(parts[1].equals(name))Oregami.add(parts[0]);
           str=br.readLine();
            }
        br.close();fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0;i<Oregami.size();i++)System.out.println("From helper "+Oregami.elementAt(i));
        return Oregami;
    }
    public static Vector<String>getRequests(String name)
    {
        Vector<String>Oregami=new Vector();
        try{
             FileInputStream fis=new FileInputStream("friendrequest.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
        
        String str=br.readLine();
        while(str!=null)
        {
            
           String [] parts=str.split("_");
           if(parts[0].equals(name))Oregami.add(parts[1]);
          // else if(parts[1].equals("name"))Oregami.add(parts[2]);
           str=br.readLine();
            }
        br.close();fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return Oregami;
    }
    public static boolean process_request(String from,String to)
    {
        String check1=from+"_"+to;
         boolean flag=true;
        String check2=to+"_"+from;
       
         try{
             FileInputStream fis=new FileInputStream("friendrequest.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
       
        String str=br.readLine();
        while(str!=null)
        {
            if(str.equals(check1))
            {
                flag=false;
                break;
            }
            else if(str.equals(check2))
            {
               
                flag=false;break;
            }
            str=br.readLine();
          
         }
         br.close();fis.close();
        if(flag)
        {
             PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter("friendrequest.txt", true)));
           
           pr.println(check2);
             
                pr.flush();
                pr.close();
        }
       
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
         return flag;
    }
    public static void accept_request(String s)
    {
        try{
        File inputFile = new File("friendrequest.txt");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = s;
        String currentLine;

while((currentLine = reader.readLine()) != null) {
    // trim newline when comparing with lineToRemove
    String trimmedLine = currentLine.trim();
    if(trimmedLine.equals(lineToRemove)) continue;
    writer.write(currentLine + System.getProperty("line.separator"));
}           
            writer.close(); 
            reader.close(); 
            if(inputFile.exists())inputFile.delete();
            boolean successful = tempFile.renameTo(inputFile);
        
    System.out.println(successful);
    }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void add_to_friendlist(String line)
    {
         try{
              PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter("friendlist.txt", true)));
             pr.println(line);
             pr.close();
            
    }
         catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    public static boolean isfriend(String a,String b)
    {
        boolean flag=false;
        String check1=a+"_"+b;
        String check2=b+"_"+a;
           try{
             FileInputStream fis=new FileInputStream("friendlist.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
       
        String str=br.readLine();
        while(str!=null)
        {
            if(str.equals(check1))
            {
                flag=true;
                break;
            }
            else if(str.equals(check2))
            {
               
                flag=true;break;
            }
            str=br.readLine();
          
         }
         br.close();fis.close();
        
    }
           catch(Exception e)
           {
               e.printStackTrace();
           }
           return flag;
    }
    public static void off_msg(String line)
    {
        
         try{
              PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter("offlinemessage.txt", true)));
             pr.println(line);
             pr.close();
            
    }
         catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    public static Vector<String> get_message(String name)
    {
        Vector<String>v=new Vector();
    
         try{
             FileInputStream fis=new FileInputStream("offlinemessage.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
        String str=br.readLine();
        while(str!=null)
        {
            if(str.startsWith(name))
            {
                String []ara=str.split("_");
                String temp="offlinemsg_"+ara[2]+" : "+ara[1];
                v.add(temp);
            }
            str=br.readLine();
        }
    }catch(Exception e)
    {
        e.printStackTrace();
    }
            return v;
    }
}
