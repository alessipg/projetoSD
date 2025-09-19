package org.alessipg.server.infra.tcp;

//public public class JsonRouter {
//    private final UsuarioController usuarioController;
//
//    public JsonRouter(UsuarioController usuarioController) {
//      this.usuarioController = usuarioController;
//    }
//
//    public Object dispatch(String action, JsonNode payload, String correlationId) {
//      switch (action) {
//        case "usuario.criar":
//          UsuarioCreateRequest req = map(payload, UsuarioCreateRequest.class);
//          return wrap(correlationId, usuarioController.criar(req));
//        // outros cases...
//        default:
//          return error(correlationId, "Ação desconhecida");
//      }
//    }
//  }
