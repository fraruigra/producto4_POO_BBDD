package grupoTeamRocket.dao;

import grupoTeamRocket.modelo.Pedido;

import java.util.List;

public interface PedidoDAO extends DAO<Pedido, Integer>{
    List<Pedido> obtenerPorCliente (String cliente) throws DAOException;
    String obtenerArticulo(Integer id) throws DAOException;
}
