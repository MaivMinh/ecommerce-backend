package com.minh.product_service.command.aggregate;

import com.minh.product_service.command.commands.CreateCategoryCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class CategoryAggregate {
    @AggregateIdentifier
    private String id;
    private String parentId;
    private String name;
    private String description;
    private String slug;
    private String image;

    public CategoryAggregate() {
        ///  default constructor required by Axon Framework.
    }

    @CommandHandler
    public CategoryAggregate(CreateCategoryCommand command) {
        log.info("Handling CreateCategoryCommand: {}", command);
        this.id = command.getId();
        this.parentId = command.getParentId();
        this.name = command.getName();
        this.description = command.getDescription();
        this.slug = command.getSlug();
        this.image = command.getImage();
    }
}
