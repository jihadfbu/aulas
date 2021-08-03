package com.gestao.aulas.resolver;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@Service
@GraphQLApi
public class BackupService {
	
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.username}")
    private String username;


    @Value("${spring.datasource.url}")
    private String urlDB;

    @Value("${backup.commandDump}")
    private String commandDump;

    @Value("${backup.pathDump}")
    private String pathDump;
    
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void novoBackup() {
    	String aux = urlDB.substring(urlDB.indexOf("//")+2,urlDB.indexOf("?"));
    	String host = aux.substring(0,aux.indexOf(":"));
    	String database = aux.substring(aux.indexOf("/")+1);
    	String dumpCommand = commandDump+" " + database + " -h " + host + " -u " + username +" -p" + password;
    	Runtime rt = Runtime.getRuntime();
    	File test=new File(pathDump);
    	PrintStream ps;

    	try{
    	Process child = rt.exec(dumpCommand);
    	ps=new PrintStream(test);
    	InputStream in = child.getInputStream();
    	int ch;
    	while ((ch = in.read()) != -1) {
    	ps.write(ch);
    	System.out.write(ch); //to view it by console
    	}

    	InputStream err = child.getErrorStream();
    	while ((ch = err.read()) != -1) {
    	System.out.write(ch);
    	}
    	}catch(Exception exc) {
    	exc.printStackTrace();
    	}
    	}

}
