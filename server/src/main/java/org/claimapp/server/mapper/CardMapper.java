package org.claimapp.server.mapper;

import org.claimapp.common.dto.CardDTO;
import org.claimapp.server.model.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface CardMapper {
    CardDTO toDTO(Card card);
    Card toEntity(CardDTO cardDTO);
}
