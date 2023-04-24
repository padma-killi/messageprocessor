package com.tcs.ain.mp.helper;

//import gov.fda.furls.ffrm.dao.helper.util.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;

import com.tcs.ain.mp.manager.ResourceManager;

public final class DataSourceHelper
{
  public DataSourceHelper(){}
  
  /**
   * The API getContext() is to create the Context for JNDI Look up.
   * @return Context.
   */
  public static Context getContext1() throws NamingException{
     return (new InitialContext());
   }
  /**
   * The API getContext() is to create the Context for JNDI Look up.
   * @return Context.
   */
   /* private static InitialContext getContext3() throws NamingException {
        Hashtable env = new Hashtable();
        FileInputStream  fis = null;
        File serverPropsFile = null;
        Properties props = null;
        try {
 
            String libDir = System.getProperty("properties.dir");
            System.out.println("THE Directory NAME " + libDir );
            String libFile = (libDir != null ? libDir+"/ppf-interface.properties" : "ppf-interface.properties"); 
            System.out.println("THE FILE NAME " + libFile );
            serverPropsFile = new File(libFile);
            fis = new FileInputStream(serverPropsFile);
            props = new Properties();
            
            props.load(fis);
            System.out.println("the provider url " + props.getProperty("java.naming.provider.url") );
            System.out.println("the initial context " + props.getProperty("java.naming.factory.initial") );
            System.out.println("the principal " + props.getProperty("java.naming.security.principal") );
            System.out.println("the credentials " + props.getProperty("java.naming.security.credentials") );
          //  ResourceManager.getInstance().
            env.put(Context.INITIAL_CONTEXT_FACTORY,props.getProperty("java.naming.factory.initial"));
            env.put(Context.SECURITY_PRINCIPAL, props.getProperty("java.naming.security.principal"));
            env.put(Context.SECURITY_CREDENTIALS, props.getProperty("java.naming.security.credentials"));
            env.put(Context.PROVIDER_URL,props.getProperty("java.naming.provider.url"));

         System.out.println("DONE %%%%%%");
         //return new InitialContext(env);
        } 
        catch(Exception e){
           System.out.println("COMING IN  EXCEPTION");
            e.printStackTrace();
            
        }
        finally 
        {
           try
           {
                if (fis != null)
                    fis.close();
           }catch (Exception e)
           {
              e.printStackTrace();
              //Log.log(Log.INFO, "DataSourceHelper1.getContext() ..Unable to close the file input stream");
              System.out.println("DataSourceHelper1.getContext() ..Unable to close the file input stream");
           }
        }
        return new InitialContext(env);
    }*/

    /**
     * Get Initial Context to Lookup DataSource and EJB
     */
    private static InitialContext getContext2() throws NamingException {
        Hashtable env = new Hashtable();
        try {
            
            System.out.println("THE INITIAL_CONTEXT_FACTORY " + ResourceManager.getInstance().getInitialFactory() );
            System.out.println("PROVIDER _ URL " + ResourceManager.getInstance().getProviderUrl() );
            System.out.println("SECURITY PRINCIPAL " + ResourceManager.getInstance().getSecurityprincipal() );
            System.out.println("SECURITY_CREDENTIALS " +  ResourceManager.getInstance().getSecuritycredentials() );
            System.out.println("the value is " + Context.INITIAL_CONTEXT_FACTORY );
            env.put(Context.INITIAL_CONTEXT_FACTORY,ResourceManager.getInstance().getInitialFactory() );
            env.put(Context.PROVIDER_URL,ResourceManager.getInstance().getProviderUrl() );
            env.put(Context.SECURITY_PRINCIPAL, ResourceManager.getInstance().getSecurityprincipal() );
            env.put(Context.SECURITY_CREDENTIALS, ResourceManager.getInstance().getSecuritycredentials() );

            return new InitialContext(env);
        } catch(Exception e){
            System.out.println("ERROR IN INITIAL CONTEXT GETCONTEXT ");
            e.printStackTrace();
        }
        return new InitialContext(env);
    }

    




//  public static Context getInitialContext() throws NamingException
//  {
//    Hashtable env = new Hashtable();
//    //env.put("dedicated.rmicontext","true");
//   // env.put("dedicated.connection","true");
//    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
//    env.put(Context.SECURITY_PRINCIPAL, "admin");
//    env.put(Context.SECURITY_CREDENTIALS, "welcome");
//    env.put(Context.PROVIDER_URL, "ormi://localhost:23891/current-workspace-app");
//   // env.put(Context.PROVIDER_URL, "ormi://furlsapp02.fda.gov:3301/ffrm");
//    
//    
//
//    return new InitialContext(env);
//  }
   /**
   * Get DataSource using JNDI
   */
    public static DataSource getDataSource1()  {
        Context ctx = null;
        DataSource dataSource = null;
        try {
             System.out.println("COMING HERE BEFORE THE GET CONTEXT METHOD IS CALLED ");
              ctx = getContext2();
            //  ctx = new InitialContext();
            // ctx = getInitialContext();
              System.out.println("Got ctx");
              if(ctx == null)
              System.out.println("ctx null");
              dataSource = (DataSource) ctx.lookup("jdbc/ffrmdevDS");
              System.out.println("COMING AFTER GETTING THE DS " + dataSource.getConnection() );
            }
        catch (NamingException ne) {
            ne.printStackTrace();
            return null;
        }
        
        catch(Exception e) 
        {
          System.out.println("COMING HERE IN EXCEPTION " + e.getMessage() );
          e.printStackTrace();
        }
        finally
        {
              try{ if (ctx != null) ctx.close(); }catch(Exception e){ e.printStackTrace();}
        }
        
         return dataSource;
    }
   /**
  * The API getConnection is used to get connection from the connection pool
  * @returns Connection.
  */
  public static Connection getConnection()
  {
        Connection conn = null;
        Context ctx = null;
        try
        {
          // ctx = new InitialContext();
          ctx = getContext2();
          //getInitialContext();
          DataSource dataSource = (DataSource)ctx.lookup("jdbc/ffrmdevDS");
          conn = dataSource.getConnection();

        //  Log.log(Log.INFO, "DataSourceHelper.getConnection() ...Connection obtained successfully from JNDI LookUp");
          System.out.println("DataSourceHelper.getConnection() ...Connection obtained successfully from JNDI LookUp");
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        //  Log.log(Log.INFO, "DataSourceHelper.getConnection() ...SQLException occurred...Could not connect to the database!");
          System.out.println("DataSourceHelper.getConnection() ...SQLException occurred...Could not connect to the database!");
        }
        catch (NamingException ne)
        {
          //ne.printStackTrace();
        //  Log.log(Log.INFO, "DataSourceHelper.getConnection() ...A Naming Exception occurred and Could not load database driver!");
        //  Log.log(Log.INFO, "DataSourceHelper.getConnection() ...Trying the alternate method to get the connection!");   
          System.out.println("DataSourceHelper.getConnection() ...A Naming Exception occurred and Could not load database driver!");
          System.out.println("DataSourceHelper.getConnection() ...Trying the alternate method to get the connection!");
          
        }
        finally
        {
              try{ if (ctx != null) ctx.close(); }catch(Exception e){ e.printStackTrace();}
        }
        return conn;
  }

}