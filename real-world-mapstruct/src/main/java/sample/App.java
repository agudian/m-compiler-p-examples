package sample;

public class App
{
    public static void main( String[] args )
    {
        // this is supposed to compile,
        // as SampleMapperImpl is generated from SampleMapper using MapStruct:
        new SampleMapperImpl().mapToTarget( new Source() );
    }
}
