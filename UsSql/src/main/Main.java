package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import funcions.Put;

public class Main {

	public static Connection  conn = null;
	
	public static void main(String[] args) {
		
		try {
			/**
			 * Carrega el driver MySQL
			 */
			Class.forName("com.mysql.jdbc.Driver");
			
			/**
			 * Connexió amb la BD
			 */
			String login = "root", pass = "", bd = "empresa";
			String url = "jdbc:mysql://localhost/" + bd;
			/**
			 * Crear la connexió
			 */
			conn = DriverManager.getConnection(url, login, pass);
			
			/**
			 * Llança la consulta
			 */
//			Statement s = conn.createStatement();
//			ResultSet resu = s.executeQuery("select nom from clients");
//			while(resu.next()){
//				Put.ln(resu.getString("nom"));
//			}
			
			mostrarClients(conn, 2);
			Put.ln("pacoooo");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param Rep la connexió com a paràmetre
	 * @return Retorna la quantitat total de cliens
	 */
	public static int qClients(Connection c) {
		
		PreparedStatement ps = null;
		
		try {
			ps = c.prepareStatement("select count(*) from clients");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next())
				return resu.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	/**
	 * 
	 * @param Rep la connexió com a paràmetre
	 * @return Retorna la quantitat total de empleats
	 */
	public static int qEmpleats(Connection c) {
		
		PreparedStatement ps = null;
		
		try {
			ps = c.prepareStatement("select count(*) from empleats");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next())
				return resu.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * 
	 * @param Rep la connexió com a paràmetre
	 * @return Retorna el deute total dels clients
	 */
	public static int deuteTotal(Connection c) {
		
		PreparedStatement ps = null;
		int q = 0;
		
		try {
			ps = c.prepareStatement("select deute from clients");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next())
				q += resu.getInt("deute");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return q;
	}
	
	/**
	 * 
	 * @param Rep la connexió com a paràmetre
	 * @return Retorna la mitjana del sou
	 */
	public static float souMig(Connection c) {
		
		PreparedStatement ps = null;
		float q = 0;
		
		try {
			ps = c.prepareStatement("select sou from empleats");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next())
				q += resu.getInt("sou");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return q/qEmpleats(c);
	}
	
	
	public static void mostrarClients(Connection c, int codi) {
		
		if(clientExists(c, codi)){
			PreparedStatement ps = null;
			
			try {
				if(codi != -1)
					ps = c.prepareStatement("select * from clients where clients.codi = " + codi);
				else
					ps = c.prepareStatement("select * from clients");	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ResultSet resu = ps.executeQuery();
				Put.ln("codi \t  nom \t\tedat \tdeute");
				while(resu.next()){
					if(resu.getString(2).length() > 5 )
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t " + resu.getInt(3)  + "\t" + resu.getInt(4));
					else
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t\t " + resu.getInt(3)  + "\t" + resu.getInt(4));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else
			Put.ln("No s'ha trobat el client.");
	}
	
	/**
	 * 
	 * @param Connexió a la base de dades
	 * @param Codi del client a buscar
	 * @return Returna un booleà per a indicar si el client existeix o no
	 */
	public static boolean clientExists(Connection c, int codi) {
		
		PreparedStatement ps = null;
		
		try {
			ps = c.prepareStatement("select codi from clients");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next())
				if(resu.getInt("codi") == codi || (codi == -1))
					return true;
				else
					return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
