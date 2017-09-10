/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recommendationsystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Bhailoo
 */
class read_place extends RecommendationSystem {
    float[][] userprofile=new float[total_users.total_users][total_users.total_attributes];
    public read_place() throws FileNotFoundException, IOException {
        BufferedReader reader ;
        String line=null;
        Scanner scanner = null;
        
        
        String[] userid = new String[total_users.total_users];
        float[][] userprofile=new float[total_users.total_users][total_users.total_attributes];
        int i=0;
        int user_id=0;
        int attribute = 0;
         reader = new BufferedReader(new FileReader("src/consumerprofile.csv"));
         line=reader.readLine();
         
         line=reader.readLine();
         while(line!=null)
         {
             
                           
             scanner = new Scanner(line);
                scanner.useDelimiter(",");
                userid[i++]=scanner.next();
                attribute=0;
                
                while(attribute<total_users.total_attributes-2)
                {
                    userprofile[user_id][attribute]=Float.parseFloat(scanner.next());
                    attribute++;
                }
                
                user_id++;
                
                line=reader.readLine();
                
                
         }
        return userprofle;
    }
    
}
