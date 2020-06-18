package com.anegocios.puntoventa.servicios;

import com.anegocios.puntoventa.jsons.CajaResponseDTO;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.jsons.CrearCuentaDTO;
import com.anegocios.puntoventa.jsons.CrearCuentaResponseDTO;
import com.anegocios.puntoventa.jsons.ModificacionPedidoDTO;
import com.anegocios.puntoventa.jsons.GrupoDTO;
import com.anegocios.puntoventa.jsons.LoginDTO;
import com.anegocios.puntoventa.jsons.LoginResponseDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.RecibirAbonoDTO;
import com.anegocios.puntoventa.jsons.RecuperarContraseniaDTO;
import com.anegocios.puntoventa.jsons.RecuperarContraseniaResponseDTO;
import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.anegocios.puntoventa.jsons.ReporteTicketDetalleDTO;
import com.anegocios.puntoventa.jsons.TicketDTO;
import com.anegocios.puntoventa.jsons.TicketPVDTO;
import com.anegocios.puntoventa.jsons.VersionEscritorioDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("ws_login_V3")
    Call<LoginResponseDTO> login(@Body LoginDTO login);

    @POST("ws_CrearCuenta_V3")
    Call<CrearCuentaResponseDTO> crearCuenta(@Body CrearCuentaDTO cc);

    @POST("ws_RecuperarContrasenia")
    Call<RecuperarContraseniaResponseDTO> recuperarContrasenia(@Body RecuperarContraseniaDTO rc);

    @POST("ws_productos_V3")
    Call<ProductoDTO> mandarConsultarProductos(@Body ProductoDTO cc);

    @POST("ws_clientes_V3")
    Call<ClienteDTO> mandarConsultarClientes(@Body ClienteDTO cc);

    @POST("ws_gruposVR_V3")
    Call<GrupoDTO> mandarConsultarGrupos(@Body GrupoDTO cc);

    @POST("ws_cajas_V3")
    Call<CajaResponseDTO> mandarCaja(@Body CajaResponseDTO cc);

    @POST("ws_tickets_V3")
    Call<TicketDTO> mandarTicket(@Body TicketDTO cc);

    @POST("ws_PV_V3")
    Call<TicketPVDTO> mandarTicketPV(@Body TicketPVDTO cc);

    @POST("ws_ReporteVentas_V3")
    Call<ReporteDTO> consultarReporte(@Body ReporteDTO cc);

    @POST("ws_DetalleTicket_V3")
    Call<ReporteTicketDetalleDTO> consultaDetalleTicket(@Body ReporteTicketDetalleDTO cc);

    @POST("ws_VersionEscritorio_V3")
    Call<VersionEscritorioDTO> consultaIniciarSesion(@Body VersionEscritorioDTO cc);

    @POST("ws_EntregarProductos_V3")
    Call<ModificacionPedidoDTO> entregarProducto(@Body ModificacionPedidoDTO cc);

    @POST("ws_AbonarPedido_V3")
    Call<RecibirAbonoDTO> recibirAbono(@Body RecibirAbonoDTO cc);

    @POST("ws_EditarPedido_V3")
    Call<TicketPVDTO> editarPedido(@Body TicketPVDTO cc);






}
