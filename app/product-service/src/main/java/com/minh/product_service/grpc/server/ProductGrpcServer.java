package com.minh.product_service.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import product_service.*;

@RequiredArgsConstructor
@GrpcService
@Service
public class ProductGrpcServer extends ProductServiceGrpc.ProductServiceImplBase {
    @Override
    public void findProductVariantById(FindProductVariantByIdRequest request, StreamObserver<FindProductVariantByIdResponse> responseObserver) {
        super.findProductVariantById(request, responseObserver);
    }

    @Override
    public void findProductVariantsByIds(FindProductVariantsByIdsRequest request, StreamObserver<FindProductVariantsByIdsResponse> responseObserver) {
        super.findProductVariantsByIds(request, responseObserver);
    }

    @Override
    public void findProductVariantByListId(FindProductVariantByListProductVariantIdRequest request, StreamObserver<FindProductVariantByListProductVariantIdResponse> responseObserver) {
        super.findProductVariantByListId(request, responseObserver);
    }
}
