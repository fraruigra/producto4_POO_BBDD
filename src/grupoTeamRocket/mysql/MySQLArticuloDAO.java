package grupoTeamRocket.mysql;

import grupoTeamRocket.dao.ArticuloDAO;
import grupoTeamRocket.dao.DAOException;
import grupoTeamRocket.modelo.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLArticuloDAO implements ArticuloDAO {

    final String INSERT = "INSERT INTO articulo (id_articulo, descripcion, precio, gastos_envio, tiempo_preparacion) VALUES (?,?,?,?,?);";
    final String GETALL = "SELECT id_articulo, descripcion, precio, gastos_envio, tiempo_preparacion FROM articulo";
    final String GETONE = "SELECT id_articulo, descripcion, precio, gastos_envio, tiempo_preparacion FROM articulo WHERE id_articulo = ?";
    private Connection conn;

    public MySQLArticuloDAO(Connection conn){
        this.conn = conn;
    }

    public MySQLArticuloDAO() {

    }

    private Articulo convertir (ResultSet rs) throws SQLException{
        String id = rs.getString("id_articulo");
        String descripcion = rs.getString("descripcion");
        Float precio = rs.getFloat("precio");
        Float gastos_envio = rs.getFloat("gastos_envio");
        int tiempo_preparacion = rs.getInt("tiempo_preparacion");
        Articulo articulo = new Articulo(id, descripcion, precio, gastos_envio, tiempo_preparacion);

        return articulo;
    }
    @Override
    public void insertar(Articulo a) throws DAOException{
        PreparedStatement stat = null;
        try {
            conn = new MySQLDAOManager().conectar();
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getIdArticulo());
            stat.setString(2, a.getDescripcion());
            stat.setFloat(3, a.getPrecio());
            stat.setFloat(4, a.getGastosEnvio());
            stat.setInt(5, a.getTiempoPreparacion());
            stat.executeUpdate();

        } catch (SQLException ex){
          throw new DAOException("Error en SQL", ex);
        } finally {
            if(stat != null){
                try {
                    stat.close();
                } catch (SQLException ex){
                    throw new DAOException("Error en SQL", ex);
                }
            }

        }
        try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modificar(Articulo a) throws DAOException {

    }

    @Override
    public void eliminar(Articulo a) throws DAOException {

    }

    @Override
    public List<Articulo> obtenerTodos() throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        ResultSet rs = null;
       List<Articulo> articulos = new ArrayList<>();
       try{
           stat = conn.prepareStatement(GETALL);
           rs = stat.executeQuery();
           while (rs.next()){
               articulos.add(convertir(rs));
           }
       } catch (SQLException ex){
           throw new DAOException("Error en SQL", ex);
       } finally {
           if (rs != null){
               try {
                   rs.close();
               } catch (SQLException ex){
                   new DAOException("Error en SQL", ex);
               }
           }
           if (stat != null){
               try {
                   stat.close();
               } catch (SQLException ex){
                   new DAOException("Error en SQL", ex);
               }
           }try {
               conn.close();
               System.out.println("Se ha desconectado de la bbdd");
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
       return articulos;
    }

    @Override
    public Articulo obtener(String id) throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        ResultSet rs = null;
        Articulo a = null;
        try{
            stat = conn.prepareStatement(GETONE);
            stat.setString(1, id);
            rs = stat.executeQuery();
            System.out.println("Hace la query");
            if(rs.next()){
                a = convertir(rs);
            } else {
                throw new DAOException("No se ha encontrado ese registro.");
            }

        }catch (SQLException ex){
            throw new DAOException("Error en SQL", ex);
        }finally {
            if(rs != null){
                try{
                    rs.close();
                }catch (SQLException ex){
                    new DAOException("Error en SQL", ex);
                }
            }
            if(stat != null){
                try{
                    stat.close();
                }catch (SQLException ex){
                    new DAOException("Error en SQL", ex);
                }
            }
        }
        return a;
    }
   /* public static void main(String[] args) throws DAOException, SQLException {
        Connection conn = null;
        final String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection("jdbc:mysql://teamrocketmysql.mysql.database.azure.com:3306/teamrocket", "Administrador", "Pokemon1234");
            ArticuloDAO dao = new MySQLArticuloDAO(conn);
            Articulo a = new Articulo("rhydty", "verrde", 34, 3, 6);
           List<Articulo> articulos = dao.obtenerTodos();
            for (Articulo b : articulos) {
                System.out.println(b.toString());
            }
            dao.obtener("aa");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }/*/
}
