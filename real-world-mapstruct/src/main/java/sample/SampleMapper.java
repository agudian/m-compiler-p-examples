package sample;

import org.mapstruct.Mapper;

@Mapper
public interface SampleMapper
{
    Target mapToTarget( Source source );
}
