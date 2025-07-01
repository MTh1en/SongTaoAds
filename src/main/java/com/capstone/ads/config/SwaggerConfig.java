package com.capstone.ads.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${app.base.url}")
    private String baseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(baseUrl))
                .info(new Info().title("SongTaoAds API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addTagsItem(new Tag().name("AUTH").description("Xác thực người dùng"))
                .addTagsItem(new Tag().name("VERIFICATION").description("Xác minh người dùng"))
                .addTagsItem(new Tag().name("USER").description("Tài khoản hệ thống"))
                .addTagsItem(new Tag().name("CUSTOMER DETAIL").description("Thông tin doanh nghiệp"))
                .addTagsItem(new Tag().name("CUSTOMER CHOICE").description("Loại sản phẩm khách hàng chọn"))
                .addTagsItem(new Tag().name("CUSTOMER CHOICE COST").description("Giá cả khách hàng chọn"))
                .addTagsItem(new Tag().name("CUSTOMER CHOICE SIZE").description("Kích thước khách hàng chọn"))
                .addTagsItem(new Tag().name("CUSTOMER CHOICE DETAIL").description("Thuộc tính sản phẩm khách hàng chọn"))
                .addTagsItem(new Tag().name("DESIGN TEMPLATE").description("Thiết kế mẫu dùng cho AI"))
                .addTagsItem(new Tag().name("BACKGROUND").description("Background mẫu khi không dùng AI"))
                .addTagsItem(new Tag().name("AI DESIGN").description("Hình ảnh chỉnh sửa từ ảnh AI tạo"))
                .addTagsItem(new Tag().name("CUSTOM DESIGN REQUEST").description("Yêu cầu thiết kế riêng"))
                .addTagsItem(new Tag().name("PRICE PROPOSAL").description("Báo giá thiết kế"))
                .addTagsItem(new Tag().name("DEMO DESIGN").description("Demo cho yêu cầu thiết kế"))
                .addTagsItem(new Tag().name("ORDER").description("Đơn hàng"))
                .addTagsItem(new Tag().name("CONTRACT").description("Hợp đồng"))
                .addTagsItem(new Tag().name("PAYMENT").description("Thanh toán"))
                .addTagsItem(new Tag().name("TICKET").description("Yêu cầu hỗ trợ"))
                .addTagsItem(new Tag().name("FEEDBACK").description("Đánh giá"))
                .addTagsItem(new Tag().name("PRODUCT TYPE").description("Loại sản phẩm"))
                .addTagsItem(new Tag().name("COST TYPE").description("Loại chi phí"))
                .addTagsItem(new Tag().name("PRODUCT TYPE SIZE").description("Kích thước sản phâm sử dụng"))
                .addTagsItem(new Tag().name("SIZE").description("Kích thước"))
                .addTagsItem(new Tag().name("ATTRIBUTE").description("Thuốc tính"))
                .addTagsItem(new Tag().name("ATTRIBUTE VALUE").description("Giá trị thuộc tính"))
                .addTagsItem(new Tag().name("CHAT BOT").description("Ai chat"))
                .addTagsItem(new Tag().name("STABLE DIFFUSION").description("Ai image"))
                .addTagsItem(new Tag().name("AWS S3").description("Xử lý file với S3"))
                ;
    }
}
