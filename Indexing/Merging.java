package Indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

class list
{
    String term;
    String postingList;
    int file;
}

class Titlelist
{
    Long Id;
    String title;
    int file;
}

class Sort implements Comparator<list> 
{ 
    public int compare(list x, list y) 
    { 
        return x.term.compareTo(y.term);
    } 
}

class SortTitle implements Comparator<Titlelist> 
{ 
    public int compare(Titlelist x, Titlelist y) 
    { 
        return x.Id.compareTo(y.Id);
    } 
}

public class Merging {
	
	public static void merge(int val){
   
		try{
			
			BufferedReader br[] = new BufferedReader[val+1];
			
	        for(int i=1;i<=val;i++)
				br[i] = new BufferedReader(new FileReader("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/file"+i));
	        
	        boolean[] flag = new boolean[val+1];
	        Comparator<list> c = new Sort();
	        PriorityQueue<list> pq = new PriorityQueue<list>(val+1,c);
	        
	        File out = new File("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/PrimaryIndex");
		    if (!out.exists())
				out.createNewFile();
	    
	    	FileWriter fw = new FileWriter(out.getAbsoluteFile());
		    BufferedWriter bw = new BufferedWriter(fw);
		    
		    for(int i=1;i<=val;i++)
		    	flag[i]=true;
		    
		    String line;
		    int comp=0;
		    
		    for(int i=1;i<=val;i++)
	        {
	            if(flag[i])
	            {                  
	               line = br[i].readLine();
	               if(line==null)
	               {
	                    comp++;
	                    flag[i]=false;    
	                    br[i].close();
	               }
	               else
	               {	       
	            	   if(line.contains("=")){
		                    String word = line.split("=")[0];
		                  
		                    list l = new list();
		                    l.postingList=line.split("=")[1];
		                    l.term=word;
		                    l.file=i;
		                    pq.add(l);
	            	   }
	               }
	            }
	        }
		    
		    list top=pq.poll();
		    int curr=top.file;
		    String term=top.term,term1=null,postl1=null;
		    
		    StringBuilder postl=new StringBuilder();
		    Long len=0L,len1=0L;
		    postl.append(top.postingList);
		    long cnt=0,cnt1=0;
		    
		    File out1 = new File("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/SecondaryIndex");
		    if (!out1.exists())
				out1.createNewFile();
	    
	    	FileWriter fw1 = new FileWriter(out1.getAbsoluteFile());
		    BufferedWriter bw1 = new BufferedWriter(fw1);
		    
		    File out2 = new File("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/TertiaryIndex");
		    if (!out2.exists())
				out2.createNewFile();
	    
	    	FileWriter fw2 = new FileWriter(out2.getAbsoluteFile());
		    BufferedWriter bw2 = new BufferedWriter(fw2);
		    
		    while(comp<val){
		    	
		        if(flag[curr]){
		        	                
	                line = br[curr].readLine();
	                if(line==null)
	                {
	                    if(flag[curr])
	                       comp++;
	                    
	                    flag[curr]=false;    
	                    br[curr].close();
	                }
	                else
	                {
	              
	                	if(line.contains("=")){
	                		
		                    String word = line.split("=")[0];
		                   
		                    list l = new list();
		                    l.postingList=line.split("=")[1];
		                    l.term=word;
		                    l.file=curr;
		                    pq.add(l);
	                	}
	                    
	                    list top1 = pq.poll();
	                    
	                    postl1=top1.postingList;
	                    term1=top1.term;
	                    curr=top1.file;
	                }
	            }
		        else{
	                list top1 = pq.poll();
	                
	                postl1=top1.postingList;
	                term1=top1.term;
	                curr=top1.file;
		        }
                
                if(term.equals(term1) && term!="")
                	postl.append(postl1);
                else{
                	     
                	if(cnt%100==0){
                        if (term.length()>0){
	                        if(cnt1%100==0){
	    				    	bw2.write(term);
	    				    	bw2.write("="+len1);
	    				    	bw2.write("\n");
	    				    }
	                        bw1.write(term);
	                        bw1.write("="+len);
	                        bw1.write("\n");
	                        
	                        cnt1++;
	                        len1=len1+term.length()+len.toString().length()+2;
                		}
                    } 
				   
				    if(term.length()>0){				    	
						bw.write(term+"=");
						bw.write(Ranking.sortByRank(postl.toString()));
						bw.write("\n");
						len=len+term.length()+postl.length()+2;
					    cnt++;
				    }
					term=term1;
					postl.setLength(0);
					postl.append(postl1);
					term1="";
					postl1="";
                }
		    } 
		    bw.close();
		    bw1.close();
		    bw2.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void mergeTitle(int val){
		   
		try{
			
			BufferedReader br[] = new BufferedReader[val+1];
			
	        for(int i=1;i<=val;i++)
				br[i] = new BufferedReader(new FileReader("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/TitleIndex"+i));
	        
	        boolean[] flag = new boolean[val+1];
	        Comparator<Titlelist> c = new SortTitle();
	        PriorityQueue<Titlelist> pq = new PriorityQueue<Titlelist>(val+1,c);
	        
	        File out = new File("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/PrimaryTitleIndex");
		    if (!out.exists())
				out.createNewFile();
	    
	    	FileWriter fw = new FileWriter(out.getAbsoluteFile());
		    BufferedWriter bw = new BufferedWriter(fw);
		    
		    for(int i=1;i<=val;i++)
		    	flag[i]=true;
		    
		    String line;
		    int comp=0;
		    
		    for(int i=1;i<=val;i++)
	        {
	            if(flag[i])
	            {                  
	               line = br[i].readLine();
	               if(line==null)
	               {
	                    comp++;
	                    flag[i]=false;    
	                    br[i].close();
	               }
	               else
	               {	       
	            	   if(line.contains(":")){
		                  
		                    Titlelist l = new Titlelist();
		                    l.title=line.substring(line.indexOf(':')+1);
		                    l.Id=Long.parseLong(line.substring(0,line.indexOf(':')));
		                    l.file=i;
		                    pq.add(l);
	            	   }
	               }
	            }
	        }
		    
		    Titlelist top=pq.poll();
		    int curr=top.file;

		    Long id=top.Id,id1=0L;
		    String title=top.title,title1="";
		    long len=0;
		    int cnt=0;
		    
		    File out1 = new File("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/SecondaryTitleIndex");
		    if (!out1.exists())
				out1.createNewFile();
	    
	    	FileWriter fw1 = new FileWriter(out1.getAbsoluteFile());
		    BufferedWriter bw1 = new BufferedWriter(fw1);
		    
		    while(comp<val){
		    	
		        if(flag[curr]){
		        	                
	                line = br[curr].readLine();

	                if(line==null)
	                {
	                    comp++;
	                    flag[curr]=false;    
	                    br[curr].close();
	                }
	                else
	                {
	                	
	                    top = new Titlelist();
	                    top.title=line.substring(line.indexOf(':')+1);
	                    top.Id=Long.parseLong(line.substring(0,line.indexOf(':')));
	                    top.file=curr;
	                    pq.add(top);
                	
	                    top = pq.poll();
	                    
	                    title1=top.title;
	                    id1=top.Id;
	                    curr=top.file;
	             
	                }
	            }
		        else{
	                top = pq.poll();
	                
	                title1=top.title;
	                id1=top.Id;
	                curr=top.file;
		        }
                    
            	if(cnt%100==0 && id!=0){
                    //System.out.println(term+"="+len+"\n");   
                    bw1.write(id+":"+len);
                    bw1.write("\n");
                } 
			    

			    if(id!=0){
					bw.write(id+":");
					bw.write(title);
					bw.write("\n");
					len=len+Long.toString(id).length()+title.length()+2;
					cnt++;
			    }
				id=id1;
				title=title1;
				id1=0L;
				title1="";
		    } 
		    bw.close();
		    bw1.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
