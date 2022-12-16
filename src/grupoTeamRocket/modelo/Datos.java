package grupoTeamRocket.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import grupoTeamRocket.dao.DAOException;
import grupoTeamRocket.dao.DAOFactory;

/*
La clase Datos es la clase principal del paquete del modelo, puesto que
contiene y gestiona todos los datos de la aplicación y es el enlace entre
el controlador y el resto de las clases del modelo ya que el controlador solo
llamará a los métodos de esta clase
 */
public class Datos {
    private ListaArticulos listaArticulos;
    private ListaClientes listaClientes;
    private ListaPedidos listaPedidos;


    public Datos() {
        listaArticulos = new ListaArticulos();
        listaClientes = new ListaClientes();
        listaPedidos = new ListaPedidos();

    }

    public ListaArticulos getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(ListaArticulos listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public ListaClientes getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(ListaClientes listaClientes) {
        this.listaClientes = listaClientes;
    }

    public ListaPedidos getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(ListaPedidos listaPedidos) {
        this.listaPedidos = listaPedidos;
    }


    public void aniadirArticulo(String id, String descripcion, float precio, float gastosEnvio, int tiempoPreparacion){

        //listaArticulos.add(new Articulo(id, descripcion, precio, gastosEnvio, tiempoPreparacion));
        try {
            DAOFactory.getDAOFactory().getArticuloDAO().insertar(new Articulo(id, descripcion, precio, gastosEnvio, tiempoPreparacion));
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList recorrerTodosArticulos(){
        ArrayList<String> arrArticulos = new ArrayList<>();
        /*for(Articulo a : listaArticulos.lista){
            arrArticulos.add(a.toString());
        }*/
        try {
            for(Articulo a : DAOFactory.getDAOFactory().getArticuloDAO().obtenerTodos()){
                arrArticulos.add(a.toString());
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return arrArticulos;
    }

    public void aniadirCliente(String nombre, String domicilio, String nif, String email, Float descuento) {

        try {
            DAOFactory.getDAOFactory().getClienteDAO().insertar(new Cliente(nombre, domicilio, nif, email) {
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
            });
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        if (descuento != null) {
            //listaClientes.add(new ClientePremium(nombre, domicilio, nif, email, descuento));
            try {
                DAOFactory.getDAOFactory().getClientePremiumDAO().insertar(new ClientePremium(nombre, domicilio, nif, email, descuento));
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //listaClientes.add(new ClienteEstandar(nombre, domicilio, nif, email));
            try {
                DAOFactory.getDAOFactory().getClienteEstandarDAO().insertar(new ClienteEstandar(nombre, domicilio, nif, email));
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList recorrerTodosClientes(){
        ArrayList<String> arrClientes = new ArrayList<>();
        try {
            for(Cliente c : DAOFactory.getDAOFactory().getClienteDAO().obtenerTodos()){
                arrClientes.add(c.toString());
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return arrClientes;
    }
    public ArrayList recorrerClienteE() {
        ArrayList<String> arrClienteEstandar = new ArrayList<>();
        try {
            for(ClienteEstandar ce : DAOFactory.getDAOFactory().getClienteEstandarDAO().obtenerTodos()){
                arrClienteEstandar.add(ce.toString());
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return arrClienteEstandar;
    }

    public ArrayList recorrerClienteP() {
        ArrayList<String> arrClientePremium = new ArrayList<>();
        try {
            for(ClientePremium cp : DAOFactory.getDAOFactory().getClientePremiumDAO().obtenerTodos()){
                arrClientePremium.add(cp.toString());
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return arrClientePremium;
    }

    public boolean aniadirPedido(int numPedido, int cantidad, LocalDateTime fecha, String email, String id) {
            try {
                DAOFactory.getDAOFactory().getPedidoDAO().insertar(new Pedido(numPedido, cantidad, fecha,
                        DAOFactory.getDAOFactory().getClienteDAO().obtener(email), DAOFactory.getDAOFactory().getArticuloDAO().obtener(id)));
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            return true;
    }


    public boolean existeCliente(String email) {
        try {
            if(email.equals(DAOFactory.getDAOFactory().getClienteDAO().obtener(email).getEmail())){
                    return true;
            }
        } catch (DAOException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }


    public void borrarPedido(int numPedido){
        try {
            if(numPedido == DAOFactory.getDAOFactory().getPedidoDAO().obtener(numPedido).getNumPedido() && pedidoEnv(numPedido) == true){
                Pedido p = DAOFactory.getDAOFactory().getPedidoDAO().obtener(numPedido);
                DAOFactory.getDAOFactory().getPedidoDAO().eliminar(p);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean pedidoEnv(int numPedido){
        LocalDateTime hoy = LocalDateTime.now();

        try {
            if((DAOFactory.getDAOFactory().getPedidoDAO().obtener(numPedido).getFecha().plusMinutes
                    (DAOFactory.getDAOFactory().getArticuloDAO().obtener(dameArticulo(numPedido)).getTiempoPreparacion()).isBefore(hoy))){
                return true;
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public ArrayList<String> pendientes(){
        ArrayList<String> arrPedido = new ArrayList<>();
        for(Pedido p : listaPedidos.lista){
            if(p.pedidoEnviado() == false){
                arrPedido.add(p.toString());
            }
        }
        return arrPedido;
    }

    public ArrayList<String> filtroPendiente(String email){
        ArrayList<String> filtro = new ArrayList<>();
        for(Pedido p : listaPedidos.lista){
            if(p.getCliente().getEmail().equals(email) && p.pedidoEnviado() == false){
                filtro.add(p.toString());
            }
        }
        return filtro;
    }

    public ArrayList<String> enviados(){
        ArrayList<String> arrPedido = new ArrayList<>();
        for(Pedido p : listaPedidos.lista){
            if(p.pedidoEnviado() == true){
                arrPedido.add(p.toString());
            }
        }
        return arrPedido;
    }
    public ArrayList<String> filtroEnviado(String email){
        ArrayList<String> filtro = new ArrayList<>();
        for(Pedido p : listaPedidos.lista){
            if(p.getCliente().getEmail().equals(email) && p.pedidoEnviado() == true){
                filtro.add(p.toString());
            }
        }
        return filtro;
    }
    public String dameArticulo (Integer id){
        String id_articulo = null;
        try {
            id_articulo = DAOFactory.getDAOFactory().getPedidoDAO().obtenerArticulo(id);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return id_articulo;
    }
}




