package grupoTeamRocket.mysql;

import grupoTeamRocket.dao.ClientePremiumDAO;
import grupoTeamRocket.dao.DAOException;
import grupoTeamRocket.modelo.ClienteEstandar;
import grupoTeamRocket.modelo.ClientePremium;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLClientePremiumDAO implements ClientePremiumDAO {
    final String INSERT = "INSERT INTO cliente_premium (fk_premium_email, nombre, nif, domicilio, descuento) VALUES (?,?,?,?,?);";
    final String GETALL = "SELECT fk_premium_email, nombre, nif, domicilio, descuento FROM cliente_premium";
    private Connection conn;
    public MySQLClientePremiumDAO(Connection conn) {
        this.conn = conn;
    }

    public MySQLClientePremiumDAO() {

    }

    @Override
    public void insertar(ClientePremium a) throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getEmail());
            stat.setString(2, a.getNombre());
            stat.setString(3, a.getNif());
            stat.setString(4, a.getDomicilio());
            stat.setFloat(5, a.getDescuento());
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

        }try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modificar(ClientePremium a) {

    }

    @Override
    public void eliminar(ClientePremium a) {

    }
    private ClientePremium convertir (ResultSet rs) throws SQLException{
        String email = rs.getString("fk_premium_email");
        String nombre = rs.getString("nombre");
        String nif = rs.getString("nif");
        String domicilio = rs.getString("domicilio");
        Float descuento = rs.getFloat("descuento");
        ClientePremium cp = new ClientePremium(nombre, domicilio, nif, email, descuento);

        return cp;
    }
    @Override
    public List<ClientePremium> obtenerTodos() throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<ClientePremium> clientePremiums = new ArrayList<>();
        try{
            stat = conn.prepareStatement(GETALL);
            rs = stat.executeQuery();
            while (rs.next()){
                clientePremiums.add(convertir(rs));
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
            }
        }try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientePremiums;
    }

    @Override
    public ClientePremium obtener(Long id) {
        return null;
    }
}
