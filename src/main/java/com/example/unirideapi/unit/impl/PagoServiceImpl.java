package com.example.unirideapi.unit.impl;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Pago;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.repository.PagoRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.unit.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {
    private final PagoRepository pagoRepository;
    private final SolicitudViajeRepository solicitudViajeRepository;

    @Override
    public PagoResponseDTO create(PagoRequestDTO pagoRequestDTO){
        SolicitudViaje solicitud = solicitudViajeRepository.findById((long)pagoRequestDTO.solicitudViajeId())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrado"));

        var pago = Pago.builder()
                .comision(pagoRequestDTO.comision())
                .estadoPago(pagoRequestDTO.estadoPago())
                .fecha(pagoRequestDTO.fecha())
                .hora(pagoRequestDTO.hora())
                .medioPago(pagoRequestDTO.medioPago())
                .monto(pagoRequestDTO.monto())
                .solicitudViaje(solicitud)
                .build();

        return toResponse(pagoRepository.save(pago));
    }

    private PagoResponseDTO toResponse(Pago pago) {
        return PagoResponseDTO.builder()
                .idPago(pago.getIdPago())
                .comision(pago.getComision())
                .estadoPago(pago.getEstadoPago())
                .fecha(pago.getFecha())
                .hora(pago.getHora())
                .medioPago(pago.getMedioPago())
                .monto(pago.getMonto())
                .solicitudViajeId(pago.getSolicitudViaje().getIdSolicitudViaje())
                .build();
    }
}
