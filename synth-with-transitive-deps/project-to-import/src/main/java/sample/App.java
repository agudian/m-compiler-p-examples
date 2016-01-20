package sample;

// this is supposed to compile,
// as the type AppDeprecation is generated using the annotation processor:
@Deprecated
public class App extends AppDeprecation
{
    public static void main( String[] args )
    {
        new App();
    }
}
