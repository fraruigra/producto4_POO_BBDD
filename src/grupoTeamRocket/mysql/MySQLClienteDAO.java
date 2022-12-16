package grupoTeamRocket.mysql;


import grupoTeamRocket.dao.ClienteDAO;
import grupoTeamRocket.dao.DAOException;
import grupoTeamRocket.modelo.Articulo;
import grupoTeamRocket.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAO implements ClienteDAO {

    final String INSERT = "INSERT INTO cliente (email, domicilio, nif, nombre) VALUES (?,?,?,?);";
    final String GETALL = "SELECT email, domicilio, nif, nombre FROM cliente";
    final String GETONE = "SELECT email, domicilio, nif, nombre FROM cliente WHERE email = ?";
    private Connection conn;

    private Cliente convertir (ResultSet rs) throws SQLException{
        String email = rs.getString("email");
        String domicilio = rs.getString("domicilio");
        String nif = rs.getString("nif");
        String nombre = rs.getString("nombre");
        Cliente cliente = new Cliente(nombre, domicilio, nif, email) {
            @Override
            public float calcAnual() {
                return 0;
            }

            @Override
            public String tipoCliente() {
                return null;
            }

            @Override
            public float descuentoEnv() {
                return 0;
            }
        };

        return cliente;
    }
    public void insertar(Cliente c) throws DAOException {
        PreparedStatement stat = null;
        try {
            conn = new MySQLDAOManager().conectar();
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, c.getEmail());
            stat.setString(2, c.getDomicilio());
            stat.setString(3, c.getNif());
            stat.setString(4, c.getNombre());
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
    public void modificar(Cliente a) throws DAOException {

    }

    @Override
    public void eliminar(Cliente a) throws DAOException {

    }

    @Override
    public List<Cliente> obtenerTodos() throws DAOException {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();
        try{
            conn = new MySQLDAOManager().conectar();
            stat = conn.prepareStatement(GETALL);
            rs = stat.executeQuery();
            while (rs.next()){
                clientes.add(convertir(rs));
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
        }
        try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    @Override
    public Cliente obtener(String id) throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        ResultSet rs = null;
        Cliente c = null;
        try{
            stat = conn.prepareStatement(GETONE);
            stat.setString(1, id);
            rs = stat.executeQuery();
            System.out.println("Hace la query");
            if(rs.next()){
                c = convertir(rs);
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
        return c;
    }
}
