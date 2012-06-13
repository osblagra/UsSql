package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import funcions.Put;
import funcions.Read;

public class Main {
	
	public static void main(String[] args) {
		
		Connection  conn = null;
		
		try {
			/** Carrega el driver MySQL */
			Class.forName("com.mysql.jdbc.Driver");
			
			/** Connexió amb la BD */
			Put.noln("Nom de la base de dades a la que conectar:");
			String login = "root", pass = "", bd = Read.Cad();
			String url = "jdbc:mysql://localhost/" + bd;
			
			/** Crear la connexió */
			try {
				conn = DriverManager.getConnection(url, login, pass);
				Put.ln("Conectat a la base de dades " + bd);
			} catch (SQLException e) {
				Put.ln("Imposible conectar amb la basse de dades.");
				Put.ln(e.getStackTrace());
			}
			
			while(true)
			 mostrarMenu(conn);
			
			/**
			 * Llança la consulta
			 */
//			Statement s = conn.createStatement();
//			ResultSet resu = s.executeQuery("select nom from clients");
//			while(resu.next()){
//				Put.ln(resu.getString("nom"));
//			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Quantitat de clients
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
	 * Quantita de empleats
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
	
	/**
	 * 
	 * @param Connexió a la base de dades
	 * @param Codi del client a buscar (-1 per a tots els disponibles)
	 */
	public static void mostrarClients(Connection c, int codi) {
		
		if(ifExists(c, codi, "clients")){
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
	 * @param Nom del client a mostrar
	 * @return Quantitat de clients amb aquest nom
	 */
	public static int mostrarClients(Connection c, String nom) {
		
		if(ifExists(c, nom, "clients")){
			PreparedStatement ps = null;
			
			try {
				ps = c.prepareStatement("select * from clients where clients.nom = \"" + nom+"\"");
				ResultSet resu = ps.executeQuery();
				Put.ln("codi \t  nom \t\tedat \tdeute");
				while(resu.next()){
					if(resu.getString(2).length() > 5 )
						Put.ln("  " + resu.getInt("codi") + "\t" + resu.getString("nom") + "\t " + resu.getInt("edat")  + "\t" + resu.getInt("deute"));
					else
						Put.ln("  " + resu.getInt("codi") + "\t" + resu.getString("nom") + "\t\t " + resu.getInt("edat")  + "\t" + resu.getInt("deute"));
					
					if(resu.last())
						return resu.getRow();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else
			Put.ln("No s'ha trobat el client.");
		
		return 0;
	}
	
	/**
	 * Mostra els empleats
	 * @param c --> Connexió a la base de dades
	 * @param codi --> Codi del empleat a mostrar, -1 mostra tots els empleats
	 */
	public static void mostrarEmpleats(Connection c, int codi) {
		
		if(ifExists(c, codi, "empleats")){
			PreparedStatement ps = null;
			
			try {
				if(codi != -1)
					ps = c.prepareStatement("select * from empleats where codi = " + codi);
				else
					ps = c.prepareStatement("select * from empleats");	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ResultSet resu = ps.executeQuery();
				Put.ln("codi \t  nom \t\tsou \tcap");
				while(resu.next()){
					if(resu.getString(2).length() > 7 )
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t " + resu.getInt(3)  + "\t " + resu.getInt(4));
					else
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t\t " + resu.getInt(3)  + "\t " + resu.getInt(4));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else
			Put.ln("No s'ha trobat el client.");
	}

	public static int mostrarEmpleats(Connection c, String nom) {
		
		if(ifExists(c, nom, "empleats")){
			PreparedStatement ps = null;
			
			try {
				ps = c.prepareStatement("select * from empleats where nom = \"" + nom + "\"");
				ResultSet resu = ps.executeQuery();
				Put.ln("codi \t  nom \t\tsou \tcap");
				while(resu.next()){
					if(resu.getString(2).length() > 7 )
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t " + resu.getInt(3)  + "\t " + resu.getInt(4));
					else
						Put.ln("  " + resu.getInt(1) + "\t" + resu.getString(2) + "\t\t " + resu.getInt(3)  + "\t " + resu.getInt(4));
					
					if(resu.last())
						return resu.getRow();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else
			Put.ln("No s'ha trobat el client.");
		
		return 0;
	}

	/**
	 * Inserir un client a la base de dades
	 * @param Connexió a la base de dades
	 */
	public static boolean insClient(Connection c) {
	
		PreparedStatement ps = null;
		String nom;
		int edat, deute;
		Put.noln("Nom del client: "); nom = Read.Cad();
		Put.noln("Edat: "); edat = Read.Int();
		Put.noln("Deute: "); deute = Read.Int();
		
		try {
			ps = c.prepareStatement("insert into clients values (0,\'"+nom+"\',"+edat+","+deute+")");
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Inserir un empleat en la base de dades
	 * @param c
	 * @return
	 */
	public static boolean insEmpleat(Connection c) {
		
		PreparedStatement ps = null;
		String nom;
		int sou, ccap;
		Put.noln("Nom de l'empleat: "); nom = Read.Cad();
		Put.noln("Sou: "); sou = Read.Int();
		mostrarEmpleats(c, -1);
		Put.noln("Codi del cap del nou empleat: "); ccap = Read.Int();
		if(ifExists(c, ccap, "empleats")) {
			mostrarEmpleats(c, ccap);
			Put.noln("Estàs segur? s/n:");
			switch(Read.carac()) {
			
			case 'S':
				try {
					ps = c.prepareStatement("insert into empleats values(0,\'"+nom+"\',"+sou+","+ccap+")");
					ps.executeUpdate();
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			
			case 'N':
				Put.ln("Operació cancelada per l'usuari.");
				return false;
			}
		}else
			Put.ln("No existeix el codi d'empleat introduït");
		
		return false;
	}
	
	/**
	 * Augmenta el sou d'un empleat segons el percentage que rep com a paràmetre
	 * @param Connexió
	 * @param Codi empleat
	 * @param percentatge augment
	 * @return
	 */
	public static int incrPrecSou(Connection c, int codi, int percentatge) {
		
		PreparedStatement ps = null;
		
		if(ifExists( c, codi, "empleats")) {
			if(codi == -1) {
				try {
					ps = c.prepareStatement("update empleats set sou = sou + (sou*"+percentatge+")/100");
					int qExecucions = ps.executeUpdate();
					
					return qExecucions;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					ps = c.prepareStatement("update empleats set sou = sou + sou*("+percentatge/100+") where codi = "+codi );
					int qExecucions = ps.executeUpdate();
					
					return qExecucions;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return 0;
	}

	/**
	 * Esborra un client
	 * @param Connexió
	 * @param codi client a esborrar
	 * @return booleà indicant si s'ha realitzat la operació
	 */
	public static boolean esborrarClient(Connection c, int codi) {
		PreparedStatement ps = null;
		
		
		if(ifExists(c, codi, "clients")){
			if(codi == -1){
				mostrarClients(c, codi);
				Put.ln("Dis-me el codi del client a esborrar:");
				return esborrarClient(c, Read.Int());
			}else{
				try {
					mostrarClients(c, codi);
					Put.ln("Vols esborrar completament el client? s/n");
					switch(Read.carac()) {
					case 's':
					case 'S':
						ps = c.prepareStatement("delete from clients where codi = "+codi);
						ps.executeUpdate();
						return true;
					default:
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			Put.ln("No existeix el client.");
			return false;
		}
		
		return false;
	}
	
	public static void mostrarMenu(Connection c) {
		Put.ln("\n\n\n\n\n\n");
		Put.ln("1. Quantitat de clients.\n2. Deute total dels clients.\n3. Sou mig dels empleats.\n4. Mostrar clients.");
		Put.ln("5. Mostrar empleats.\n6. Inserir clients.\n7. Inserir empleats.");
		Put.ln("8. Incrementar sou empleats.\n9. Esborrar clients.\n10. Eixir.");
		
		switch(Read.Int()) {
		case 1:
			Put.ln(qClients(c));
			break;
		case 2:
			Put.ln(deuteTotal(c));
			break;
		case 3:
			Put.ln(souMig(c));
			break;
		case 4:
			Put.ln("1. Busqueda per codi.\n2. Busqueda per nom.");
			switch(Read.Int()) {
			case 1:
				Put.noln("Codi del client: ");
				mostrarClients(c, Read.Int());
				break;
			case 2:
				Put.noln("Nom del client: ");
				Put.ln(mostrarClients(c, Read.Cad()) + " coincidents.");
				break;
			default:
				Put.ln("Opció no vàlida.");
			}
			break;
		case 5:
			Put.ln("1. Busqueda per codi.\n2. Busqueda per nom.");
			switch(Read.Int()) {
			case 1:
				Put.noln("Codi de l'empleat: ");
				mostrarEmpleats(c, Read.Int());
				break;
			case 2:
				Put.noln("Nom del empleat: ");
				mostrarEmpleats(c, Read.Cad());
				break;
			default:
				Put.ln("Opció no vàlida.");
			}
			break;
		case 6:
			insClient(c);
			break;
		case 7:
			insEmpleat(c);
			break;
		case 8:
			Put.noln("Codi de l'empleat: "); int codi = Read.Int();
			Put.noln("Percentatge de increment: "); int percentatge = Read.Int();
			incrPrecSou(c, codi, percentatge);
			break;
		case 9:
			Put.noln("Codi del client a esborrar: ");
			esborrarClient(c, Read.Int());
			break;
		case 10:
			try {
				c.close();
				Put.ln("Connexió tancada.");
			} catch (SQLException e) {
				Put.ln("Error al tancar la base de dades");
			}
			System.exit(0);
		default:
			Put.ln("Opció no vàlida.");
		}
	}
	
	/**
	 * @param Connexió a la base de dades
	 * @param Codi del client a buscar
	 * @return Returna un booleà per a indicar si el client existeix o no
	 */
	public static boolean ifExists(Connection c, int codi, String taula) {
		
		PreparedStatement ps = null;
		
		if(codi == -1)
			return true;
		
		try {
			ps = c.prepareStatement("select codi from " +taula);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next()){
				if(resu.getInt("codi") == codi)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param Connexió a la base de dades
	 * @param Nom a buscar
	 * @param taula on buscar
	 * @return true o false segons si existeis o no
	 */
	public static boolean ifExists(Connection c, String n, String taula) {
		
		PreparedStatement ps = null;
		
		try {
			ps = c.prepareStatement("select nom from " +taula);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ResultSet resu = ps.executeQuery();
			while(resu.next()){
				Put.ln(resu.getString("nom"));
				if((resu.getString("nom").toLowerCase()).equals(n.toLowerCase()))
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
