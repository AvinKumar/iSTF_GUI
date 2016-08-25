

package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

import com.thoughtworks.selenium.DefaultSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * TODO: Class Description
 * @author sayedmo
 */
public class TableTag extends SeleniumTagSupport {
	//static Logger logger=Logger.getLogger(TableTag.class);

	private String row; 
	//private String col;
	/* milliseconds */
    
    
    public String getRow() {
		return row;
	}


	public void setRow(String row) {
		this.row = row;
	}
	
	private String id, expected;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


	@Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
	/*	
		//DefaultSelenium selenium=getSelenium();
                WebDriver driver=getSelenium();
		String rowadd=row + "/table/tbody/tr";
		
		//int rowCount = selenium.getXpathCount(rowadd).intValue();
                int rowCount=0;//driver.findElement(By.xpath(rowadd)).size();
		//int colcount ;
		System.out.println("preview rowcount >>>>"+ rowCount);

		//String user1 = "ABC"
		
		
      int i,j;
			for(i = 1;i <=rowCount;i++)
			{
				//colcount=selenium.getXpathCount(row +"["+ i+"]//td").intValue();
			   for(j=1;j<=2 ;j++)
			   {
			//      System.out.print(driver.getText("xpath="+rowadd +"["+i+"]/td["+ j +"]"));
			      if (j==0)
			      {
			    	  System.out.print(":");
			      }
			   }
			   
			         
			}*/
		
		WebDriver driver = getSelenium();
		int loc = id.indexOf("=");
        String command = "";
        String actual = "";
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "xpath": {

		//Get number of rows In table. 
				int Row_count = driver.findElements(By.xpath("//*[@id='post-body-6522850981930750493']/div[1]/table/tbody/tr[1]/td")).size(); 
				System.out.println("Number Of Columns = "+Row_count);
				
			//Get number of columns In table. 
			int Col_count = driver.findElements(By.xpath("//*[@id='post-body-6522850981930750493']/div[1]/table/tbody/tr[1]/td")).size(); 
			System.out.println("Number Of Columns = "+Col_count);
			
			//divided xpath In three parts to pass Row_count and Col_count values. 
			String first_part = "//*[@id='post-body-6522850981930750493']/div[1]/table/tbody/tr["; 
			String second_part = "]/td["; 
			String third_part = "]";
			
			//Used for loop for number of rows. 
			for (int i=1; i<=Row_count; i++){
			//Used for loop for number of columns. 
			for(int j=1; j<=Col_count; j++){
			//Prepared final xpath of specific cell as per values of i and j. 
			String final_xpath = first_part+i+second_part+j+third_part;
			//Will retrieve value from located cell and print It. 
			String Table_data = driver.findElement(By.xpath(final_xpath)).getText();
			System.out.print(Table_data +" "); 
			}
			System.out.println(""); 
			System.out.println(""); 
			} 
			} 
			}
		
    }

}
