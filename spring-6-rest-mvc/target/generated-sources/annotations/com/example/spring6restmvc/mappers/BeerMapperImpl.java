package com.example.spring6restmvc.mappers;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-08T12:59:19-0400",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class BeerMapperImpl implements BeerMapper {

    @Override
    public Beer beerDtoToBeer(BeerDTO beerDTO) {
        if ( beerDTO == null ) {
            return null;
        }

        Beer.BeerBuilder beer = Beer.builder();

        beer.id( beerDTO.getId() );
        beer.version( beerDTO.getVersion() );
        beer.beerName( beerDTO.getBeerName() );
        beer.beerStyle( beerDTO.getBeerStyle() );
        beer.upc( beerDTO.getUpc() );
        beer.quantityOnHand( beerDTO.getQuantityOnHand() );
        beer.price( beerDTO.getPrice() );
        beer.createdDate( beerDTO.getCreatedDate() );
        beer.updateDate( beerDTO.getUpdateDate() );

        return beer.build();
    }

    @Override
    public BeerDTO beerToBeerDto(Beer beer) {
        if ( beer == null ) {
            return null;
        }

        BeerDTO.BeerDTOBuilder beerDTO = BeerDTO.builder();

        beerDTO.id( beer.getId() );
        beerDTO.version( beer.getVersion() );
        beerDTO.beerName( beer.getBeerName() );
        beerDTO.beerStyle( beer.getBeerStyle() );
        beerDTO.upc( beer.getUpc() );
        beerDTO.quantityOnHand( beer.getQuantityOnHand() );
        beerDTO.price( beer.getPrice() );
        beerDTO.createdDate( beer.getCreatedDate() );
        beerDTO.updateDate( beer.getUpdateDate() );

        return beerDTO.build();
    }
}
