package sample;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementKindVisitor6;
import javax.tools.JavaFileObject;

import com.google.common.collect.Collections2;

@SupportedAnnotationTypes( "java.lang.Deprecated" )
public class SampleProcessor
    extends AbstractProcessor
{

    @Override
    public boolean process( Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment )
    {
        // use guava class
        Collection<? extends TypeElement> filteredAnnotations = Collections2.filter( annotations, a -> {
            return a.getKind() == ElementKind.ANNOTATION_TYPE;
        } );

        for ( TypeElement annotation : filteredAnnotations )
        {
            Set<? extends Element> annotatedTypes = roundEnvironment.getElementsAnnotatedWith( annotation );
            for ( Element element : annotatedTypes )
            {
                TypeElement typeElement = asTypeElement( element );
                if ( typeElement != null && typeElement.getAnnotation( Deprecated.class ) != null )
                {
                    String className = typeElement.getSimpleName().toString() + "Deprecation";
                    String packageName =
                        processingEnv.getElementUtils().getPackageOf( typeElement ).getQualifiedName().toString();
                    JavaFileObject sourceFile;
                    try
                    {
                        sourceFile = processingEnv.getFiler().createSourceFile( packageName + "." + className );

                        try (Writer w = sourceFile.openWriter())
                        {
                            w.append( "package " ).append( packageName ).append( ";\n\n" );
                            w.append( "public abstract class " ).append( className ).append( " {\n" );
                            w.append( "    public " ).append( className ).append( "() {\n" );
                            w.append( "        System.err.println(\"Hey there, the type " )
                                .append( className )
                                .append( " is deprecated and you shouldn't use it anymore.\");\n" );
                            w.append( "    }\n}\n" );
                        }
                    }
                    catch ( IOException e )
                    {
                        throw new RuntimeException( e );
                    }
                }
            }
        }

        return false;
    }

    private TypeElement asTypeElement( Element element )
    {
        return element.accept( new ElementKindVisitor6<TypeElement, Void>()
        {
            @Override
            public TypeElement visitTypeAsInterface( TypeElement e, Void p )
            {
                return e;
            }

            @Override
            public TypeElement visitTypeAsClass( TypeElement e, Void p )
            {
                return e;
            }

        }, null );
    }
}
