package Parsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser extends DefaultHandler{

	   HashSet<String> stopWords = new HashSet<String>();
	   boolean ttl=false,flagId=false,pg=false,txt=false,rev=false;
	   StringBuilder strTtl = new StringBuilder();
	   StringBuilder strTxt = new StringBuilder();
	   String Id;
	   StringBuilder t1 = new StringBuilder();
	   StringBuilder temp = new StringBuilder();
	   int cnt=0;
	   public int docs=0;
	   public int fileno=0;
	   
	   public TreeMap<Long,String> tmap = new TreeMap<Long,String>();
	   public TreeMap<String,HashMap<String,Data>> map = new TreeMap<String,HashMap<String,Data>>(); 
	   
	   public Parser(int fileno,HashSet<String> stopWords){
		   this.fileno=fileno;
		   this.stopWords=stopWords;
	   }
	  
	   public void populate(String str,String doc,String type){
		   
		    str=str.toLowerCase();
        	str.trim();

        	if(type.equals("title")){
	        	if(str.length()==0||str.isEmpty())
	        		return;
        	}
        	else{
        		if(str.length()<=2||str.isEmpty())
        			return;
        				
        		if(stopWords.contains(str))
        			return;
        	}

        	Stemming s = new Stemming();
			s.add(str.toCharArray(),str.length());
			str=s.stem();

			if(!type.equals("title")){
				if(str.length()<=2)
	        		return;
			}

		    if(!map.containsKey(str)){
		    	
		    	HashMap<String,Data> loc_map=new HashMap<String,Data>();
	        	map.put(str, loc_map);
		    }
		    if(!map.containsKey(str) || !map.get(str).containsKey(doc))
         		map.get(str).put(doc, new Data());
		    
		    if(type.equals("title"))
		    	map.get(str).get(doc).title++;
		    else if(type.equals("text"))
		    	map.get(str).get(doc).text++;
		    else if(type.equals("infobox"))
		    	map.get(str).get(doc).infobox++;
		    else if(type.equals("category"))
		    	map.get(str).get(doc).category++;
		    else if(type.equals("extLink"))
		    	map.get(str).get(doc).extLink++;
		    else if(type.equals("ref"))
		    	map.get(str).get(doc).ref++;
	   }
	   
	   public void parseText(String temp){
		   
		   boolean flagInfobox=false;
		   boolean flagCat=false;
		   boolean flagRef=false;
		   boolean flagExt=false;
		   
		   for(int i=0;i<temp.length();i++){
			   
			   cnt=0;
			   if(temp.charAt(i)=='{'){
				   
				   if(i+9<temp.length() && temp.substring(i+1, i+9).equalsIgnoreCase("{Infobox")){
			 	   
					   flagInfobox=true;
					   boolean flag=false;
					   cnt=2;
					   i=i+9;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
						   
						   if(temp.charAt(i)=='[' && i+1<temp.length() && temp.charAt(i+1)=='['){
							   i++;
							   flag=true;
							   i++;
							   continue;
						   }
						   if(temp.charAt(i)==']' && i+1<temp.length() && temp.charAt(i+1)==']'){
							   
							   populate(strTxt.toString(),Id,"infobox");
							   strTxt.setLength(0);
							   flag=false;
						   }
						   
						   if(flag){
							   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
								   strTxt.append(temp.charAt(i));
							   else{
								   populate(strTxt.toString(),Id,"infobox");
								   strTxt.setLength(0);
							   }
						   }
						   i++;
					   }
				   }
				   else if(i+8<temp.length() && temp.substring(i+1,i+8).equalsIgnoreCase("{Geobox")){
					   
					   cnt=2;
					   i=i+8;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;

						   i++;
					   }
				   }
				   else if(i+6<temp.length() && temp.substring(i+1,i+6).equalsIgnoreCase("{cite")){
					   
					   cnt=2;
					   i=i+6;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;

						   i++;
					   }
				   }
				  else if(i+4<temp.length() && temp.substring(i+1,i+4).equals("{gr")){
										   
					   cnt=2;
					   i=i+4;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;

						   i++;
					   }
				   }
				  else if(i+7<temp.length() && temp.substring(i+1,i+7).equalsIgnoreCase("{Coord")){
					   
					   cnt=2;
					   i=i+7;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
				
						   i++;
					   }
				  }
			   }
			   else if(temp.charAt(i)=='['){

				   if(i+11<temp.length() && temp.substring(i+1, i+11).equalsIgnoreCase("[Category:")){
			   
					   flagCat=true;
					   cnt=2;
					   i=i+11;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='[')
							   cnt++;
						   if(temp.charAt(i)==']')
							   cnt--;
						   
						   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
							   strTxt.append(temp.charAt(i));
						   else{
							   populate(strTxt.toString(),Id,"category");
							   strTxt.setLength(0);
						   }
						   i++;
					   }
				   }
				   else if(i+7<temp.length() && temp.substring(i+1, i+7).equalsIgnoreCase("[File:")){

					   cnt=2;
					   i=i+7;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='[')
							   cnt++;
						   if(temp.charAt(i)==']')
							   cnt--;
						  
						   i++;
					   }
				   }
				   else if(i+8<temp.length() && temp.substring(i+1, i+8).equalsIgnoreCase("[Image:")){

					   cnt=2;
					   i=i+8;
					   while(cnt!=0 && i<temp.length()){
						   
						   if(temp.charAt(i)=='[')
							   cnt++;
						   if(temp.charAt(i)==']')
							   cnt--;
						  
						   i++;
					   }
				   }
			   }
			   else if(temp.charAt(i)=='<'){
			
				   if(i+4<temp.length() && temp.substring(i+1, i+4).equals("!--")){

					   i=i+4;
					   while(i<temp.length()){
						   if(i+3<temp.length() && temp.substring(i, i+3).equals("-->"))
							   break;
						   i++;
					   }
					   i=i+3;
				   }
				   else if(i+8<temp.length() && temp.substring(i+1, i+8).equalsIgnoreCase("gallery")){
					   
					   i=i+8;
					   while(i<temp.length()){
						   if(i+10<temp.length() && temp.substring(i, i+10).equalsIgnoreCase("</gallery>"))
							   break;
						   i++;
					   }
					   i=i+10;
				   }
				   if(i+4<temp.length() && temp.substring(i+1, i+4).equalsIgnoreCase("ref")){
					   
					   i=i+4;
					   while(i<temp.length()){
						   
						   if(i+6<temp.length() && temp.substring(i, i+6).equalsIgnoreCase("</ref>"))
								   break;
						   
						   i++;
					   }
					   i=i+6;
				   }
				   
			   }
			   else if(temp.charAt(i)=='=' && i+1<temp.length() && temp.charAt(i+1)=='='){

				   if(i+12<temp.length() && (temp.substring(i+2, i+12).equalsIgnoreCase("References"))){
					   
					   i=i+14;
					   cnt=0;
					   flagRef=true;
					   
					   while(i<temp.length() && cnt!=-2){

						   if(temp.charAt(i)=='=' && i+1<temp.length() && temp.charAt(i+1)=='='){
							   i--;
							   break;
						   }
						   if(i+11<temp.length() && temp.substring(i, i+11).equalsIgnoreCase("[[Category:"))
							   break;
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
						   
						   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
							   strTxt.append(temp.charAt(i));
						   else{
							   populate(strTxt.toString(),Id,"ref");
							   strTxt.setLength(0);
						   }
						   i++;
					   }
				   }
				   else if(i+14<temp.length() && temp.charAt(i+2)==' ' && temp.substring(i+3, i+14).equalsIgnoreCase("References ")){
				   
					   i=i+16;
					   cnt=0;
					   flagRef=true;
					   
					   while(i<temp.length() && cnt!=-2){

						   if(temp.charAt(i)=='=' && i+1<temp.length() && temp.charAt(i+1)=='='){
							   i--;
							   break;
						   }
						   if(i+11<temp.length() && temp.substring(i, i+11).equalsIgnoreCase("[[Category:"))
							   break;
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
						   
						   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
							   strTxt.append(temp.charAt(i));
						   else{
							   populate(strTxt.toString(),Id,"ref");
							   strTxt.setLength(0);
						   }
						   i++;
					   }
				   }
				   else if(i+16<temp.length() && (temp.substring(i+2, i+16).equalsIgnoreCase("External Links"))){
					  
					   i=i+18;
					   cnt=0;
					   flagExt=true;
					   
					   while(i<temp.length() && cnt!=-2){

						   if(temp.charAt(i)=='=' && i+1<temp.length() && temp.charAt(i+1)=='=')
							   break;
						   if(i+11<temp.length() && temp.substring(i, i+11).equalsIgnoreCase("[[Category:"))
							   break;
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
						   
						   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
							   strTxt.append(temp.charAt(i));
						   else{
							   populate(strTxt.toString(),Id,"extLink");
							   strTxt.setLength(0);
						   }
						   i++;
					   }
				   }
				   else if(i+18<temp.length() && temp.charAt(i+2)==' ' && temp.substring(i+3, i+18).equalsIgnoreCase("External Links ")){

					   i=i+20;
					   cnt=0;
					   flagExt=true;
					   
					   while(i<temp.length() && cnt!=-2){
						   
						   if(temp.charAt(i)=='=' && i+1<temp.length() && temp.charAt(i+1)=='=')
							   break;
						   if(i+11<temp.length() && temp.substring(i, i+11).equalsIgnoreCase("[[Category:"))
							   break;
						   
						   if(temp.charAt(i)=='{')
							   cnt++;
						   if(temp.charAt(i)=='}')
							   cnt--;
						   
						   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
							   strTxt.append(temp.charAt(i));
						   else{
							   populate(strTxt.toString(),Id,"extLink");
							   strTxt.setLength(0);
						   }
						   i++;
					   }
				   }
			   }
			   else{
				   if(temp.charAt(i)>='a' && temp.charAt(i)<='z' || temp.charAt(i)>='A' && temp.charAt(i)<='Z')
					   strTxt.append(temp.charAt(i));
				   else{
					   populate(strTxt.toString(),Id,"text");
					   strTxt.setLength(0);
				   }
			   }
		   }
		   if(flagInfobox)
			   populate(strTxt.toString(),Id,"infobox");
		   else if(flagCat)
			   populate(strTxt.toString(),Id,"category");
		   else if(flagRef)
			   populate(strTxt.toString(),Id,"ref");
		   else if(flagExt)
			   populate(strTxt.toString(),Id,"extLink");
		   else
			   populate(strTxt.toString(),Id,"text");

		   strTxt.setLength(0);
	}
	   
	   @Override
	   public void startElement(String uri, String localName, String qName, Attributes attributes)
	      throws SAXException {
		   
		   if(qName.equalsIgnoreCase("page")){
			   
			   if(docs%5000==0 && docs!=0)
	            {
				     fileno++;
		             try
		        	{
		        		write("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/file"+fileno);
		        		writeTitle("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/TitleIndex"+fileno);
		        	}catch(Exception e)
		        	{
		        		System.out.print("Exception in writing to disk");
		        	}
	            }
	            docs++;
	            pg=true;
		   }
		   else if (qName.equalsIgnoreCase("title")){
			   ttl=true;
			   strTtl.setLength(0);
			   t1.setLength(0);
		   }
		   else if (qName.equalsIgnoreCase("id") && !rev)
			   flagId=true;
		   else if (qName.equalsIgnoreCase("revision"))
			   rev=true;
		   else if (qName.equalsIgnoreCase("text")){
			   txt=true;
			   temp.setLength(0);
			   strTxt.setLength(0);
		   }
	   }

	   @Override
	   public void endElement(String uri, 
	   String localName, String qName) throws SAXException {
		   
		   if (qName.equalsIgnoreCase("title"))
		         ttl=false;
		   else if (qName.equalsIgnoreCase("id") && !rev)
		         flagId=false;
		   else if (qName.equalsIgnoreCase("text")){
		         txt=false;
		         parseText(temp.toString());
		         temp.setLength(0);
		   }
		   else if (qName.equalsIgnoreCase("revision"))
		         rev=false;
		   else if (qName.equalsIgnoreCase("page"))
		         pg=false;
	   }

	   @Override
	   public void characters(char ch[], int start, int length) throws SAXException{ 

		   if(ttl)
			   t1.append(new String(ch,start,length));
		   else if(flagId){
			   
			  Id=new String(ch,start,length);
			  StringBuilder title = new StringBuilder();
			  title.setLength(0);
			  String t=t1.toString();
			  t1.setLength(0);
			
			  for(int i=0;i<t.length();i++){
				   
				   if(t.charAt(i)>='a' && t.charAt(i)<='z' || t.charAt(i)>='A' && t.charAt(i)<='Z' || t.charAt(i)>='0' && t.charAt(i)<='9')
					   strTtl.append(t.charAt(i));
				   else{
					   if(strTtl.length()>0){
						   title.append(strTtl+" ");
						   populate(strTtl.toString(),Id,"title");
						   strTtl.setLength(0);
					   }
				   }
			   }
			   if(strTtl.length()>0){
				   title.append(strTtl);
				   populate(strTtl.toString(),Id,"title");
				   strTtl.setLength(0);
			   }
		
			   tmap.put(Long.parseLong(Id),title.toString());
		   }
		   else if (txt)
			   temp.append(new String(ch,start,length));
	   }
	   
	   public void writeTitle(String out){
		   
		   try
	    	{
			    File file1 = new File(out);
	    	
				if (!file1.exists())
					file1.createNewFile();
	
				FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
				BufferedWriter bw1 = new BufferedWriter(fw1);
				StringBuilder str = new StringBuilder();
				str.setLength(0);
				
		    	for (Entry<Long, String> entry : tmap.entrySet()) {
	    			
	    			if(entry.getValue()!=""){
	    				//str.append(entry.getKey()+":"+entry.getValue()+"\n");
	    				bw1.write(entry.getKey()+":"+entry.getValue());
	    				bw1.write("\n");
	    			}
		    	}
		    	//System.out.println(str);
		    	tmap.clear();
		    	bw1.close();
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	   }

	   public void write(String out){
		   
		   try
	    	{
			    File file = new File(out);
	    	
				if (!file.exists())
					file.createNewFile();
	
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
		    	for (Entry<String, HashMap<String, Data>> entry : map.entrySet()) 
		    	{
		    		String word=entry.getKey();
		    		HashMap<String,Data> loc_map = new HashMap<String,Data>(entry.getValue());
		    		bw.write(word+"=");

		    		for(Entry<String,Data> loc_entry:loc_map.entrySet())
		    		{
		    			String docId = loc_entry.getKey();
		    			Data obj = loc_entry.getValue();
		    			String title="",txt="",info="",cat="",ext="",ref="";
		    			String append=docId+"-";
		    			
		    			if(obj.title!=0){
		    				title=""+obj.title;
		    				append+='T'+title;
		    			}
		    			if(obj.text!=0){
		    				txt=""+obj.text;
		    				append+='B'+txt;
		    			}
		    			if(obj.category!=0){
		    				cat=""+obj.category;
		    				append+='C'+cat;
		    			}
		    			if(obj.infobox!=0){
		    				info=""+obj.infobox;
			    			append+='I'+info;
		    			}
		    			if(obj.extLink!=0){
		    				ext=""+obj.extLink;
		    				append+='E'+ext;
		    			}
		    			if(obj.ref!=0){
		    				ref=""+obj.ref;
		    				append+='R'+ref;
		    			}
		    			bw.write(append);
		    			bw.write(";");
		    			//System.out.println(word+"-"+append);
		    		}
		    		bw.write("\n");
		    	}
		    	bw.close();
		    	map.clear();
			}catch(Exception e){}
	   }
}
