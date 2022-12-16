package grupoTeamRocket.dao;

import java.sql.*;
import java.sql.Connection;
//..

public class Utilidad {

    protected Connection conexion;
    private final String url = "jdbc:mysql://teamrocketmysql.mysql.database.azure.com:3306/teamrocket";
    private final String user = "Administrador";
    private final String password = "Pokemon1234";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    Connection cx;
    public Utilidad(){

    }

    public Connection conectar(){
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url, user, password);
            System.out.println("Se conecta");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("No se conecta");
            throw new RuntimeException(e);
        }
        return cx;
    }

    public void desconectar() throws SQLException{
        if(cx != null){
            if(!cx.isClosed()){
                cx.close();
            }
        }
    }

}
