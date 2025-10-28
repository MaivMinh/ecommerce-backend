package com.minh.support_service.grpc.server;

import com.minh.support_service.service.SupportService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import support_service.GetShippingAddressRequest;
import support_service.GetShippingAddressResponse;
import support_service.SupportServiceGrpc;

@GrpcService
@RequiredArgsConstructor
public class SupportGrpcServer extends SupportServiceGrpc.SupportServiceImplBase {
    private final SupportService supportService;

    @Override
    public void getShippingAddress(GetShippingAddressRequest request, StreamObserver<GetShippingAddressResponse> responseObserver) {
        GetShippingAddressResponse response = supportService.getShippingAddress(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
