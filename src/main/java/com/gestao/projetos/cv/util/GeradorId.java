package com.gestao.projetos.cv.util;

public class GeradorId {
    private static long proximoId = 1;
    
    public static synchronized String gerarProximoId() {
        return String.format("%09d", proximoId++);
    }
    
    public static synchronized void definirProximoId(long id) {
        if (id > proximoId) {
            proximoId = id;
        }
    }
}
