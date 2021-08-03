/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestao.aulas.resolver;

/**
 *
 * @author ramonsouza
 */
class InvalidCredentialsException extends Exception {
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public InvalidCredentialsException(String message){
            super(message);
        }
    
}
