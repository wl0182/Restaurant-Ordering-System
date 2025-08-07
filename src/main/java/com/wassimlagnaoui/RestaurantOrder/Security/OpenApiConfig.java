package com.wassimlagnaoui.RestaurantOrder.Security;

import io.swagger.v3.oas.models.OpenAPI;

public class OpenApiConfig {
    //add your OpenAPI configuration here
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Restaurant Order API")
                        .version("1.0.0")
                        .description("API for managing restaurant orders and sessions"));
    }
}
