package com.minh.cart_service.grpc;

import com.minh.grpc.product_service.FindProductVariantsByIdsRequest;
import com.minh.grpc.product_service.FindProductVariantsByIdsResponse;
import com.minh.grpc.product_service.ProductServiceGrpc;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceGrpcClient {
    @GrpcClient("productService")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    private FindProductVariantsByIdsResponse findProductVariantsByIds(FindProductVariantsByIdsRequest request, Throwable throwable) {
        String message = throwable instanceof CallNotPermittedException
                ? "Circuit breaker đang ở trạng thái Open. Vui lòng thử lại sau."
                : "Có lỗi xảy ra khi gọi tới Product service: " + throwable.getMessage();
        return FindProductVariantsByIdsResponse.newBuilder()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(message)
                .build();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "findProductVariantsByIds")
    public FindProductVariantsByIdsResponse findProductVariantsByIds(FindProductVariantsByIdsRequest request) throws Exception {
        /**
         Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) )
         - Phía trên là thứ tự thực hiện (mặc định) của các decorator.
         - Bulkhead sẽ được thực hiện đầu tiên, sau đó là TimeLimiter, tiếp theo là RateLimiter, và cuối cùng là CircuitBreaker và Retry.
         - Về cơ bản, khi các exception xảy ra trong các decorator, chúng sẽ được ném ra cho các decorator phía trên.
         - Ví dụ: nếu TimeLimiter ném ra một exception, thì RateLimiter sẽ thực hiện handle exception, nếu không thì exception này sẽ ném ra cho Circuit Breaker.
         Ví du: - Hiện tại, chúng ta đang sử dụng CircuitBreaker và TimeLimiter.
         - TimeLimiter sẽ thực hiện đầu tiên, sau đó là CircuitBreaker. Nếu TimeLimiter ném ra một exception, thì CircuitBreaker sẽ xử lý exception này do chúng ta đang có một hàm fallback method là fallbackFindProductVariantById(). Nên do đó, khi timeout xảy ra người dùng sẽ nhận được response mà hàm fallback này trả về.
         - Nhưng nếu trong trường hợp mà TimeLimiter không ném ra exception mà CircuitBreaker ném ra exception(Ví dụ như Database không hoạt động), thì hàm fallbackFindProductVariantById() vẫn sẽ hoạt động và exception này sẽ được tính cho CircuitBreaker.
         - Trong trường hợp cả 2 không ném ra exception thì hàm findProductVariantsByIds() sẽ được thực hiện và trả về kết quả.
         */

        TimeLimiter timeLimiter = TimeLimiter.ofDefaults("productService");
        return timeLimiter.executeFutureSupplier(
                () -> CompletableFuture.supplyAsync(
                        () -> productServiceBlockingStub.findProductVariantsByIds(request)
                )
        );
    }
}
