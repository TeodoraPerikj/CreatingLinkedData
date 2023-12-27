import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreatingLinkedData {

    static final String foafProfile = "https://www.dropbox.com/s/rwzmqg67vc6dp76/foaf.ttl?dl=1";

    public static void main(String[] args) {

        Model foafProfileModel = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(foafProfile);

        if (in == null)
            throw new IllegalArgumentException("File: " + foafProfile + " not found.");

        foafProfileModel.read(in, "", "TURTLE");

        foafProfileModel.write(System.out, "TURTLE");
        System.out.println();

        StmtIterator foafProfileIterator = foafProfileModel.listStatements(new SimpleSelector(null, FOAF.knows,
                (RDFNode) null));

        List<String> peopleURIs = new ArrayList<>();

        if(foafProfileIterator.hasNext()){
            System.out.println("Printing URIs of people I know: ");

            while (foafProfileIterator.hasNext()){

                Statement statement = foafProfileIterator.nextStatement();

                Resource blankNode = statement.getObject().asResource();

                StmtIterator it = foafProfileModel.listStatements(new SimpleSelector(blankNode, null,
                        (RDFNode) null));

                if(it.hasNext()){
                    while (it.hasNext()){

                        Statement statement1 = it.nextStatement();

                        if(RDFS.seeAlso.equals(statement1.getPredicate())){

                            String personURI = statement1.getObject().toString();

                            peopleURIs.add(personURI);

                            System.out.println("URI: " + personURI);

                        }
                    }
                }
            }
        }

        System.out.println();

        Model peopleModel = ModelFactory.createDefaultModel();

        for (String personURI : peopleURIs) {

            Model personModel = ModelFactory.createDefaultModel();

            InputStream personInput = FileManager.get().open(personURI);

            if (personInput == null)
                throw new IllegalArgumentException("File: " + personURI + " not found.");

            personModel.read(personInput, "", "TURTLE");

            peopleModel = peopleModel.union(personModel);
        }

        peopleModel.write(System.out, "TURTLE");

        System.out.println();

        peopleModel = peopleModel.union(foafProfileModel);

        peopleModel.write(System.out, "TURTLE");

        System.out.println();

        StmtIterator foafNames = peopleModel.listStatements(new SimpleSelector(null, FOAF.name, (RDFNode) null));

        if (foafNames.hasNext()) {
            System.out.println("Printing names: ");

            while (foafNames.hasNext()) {

                Statement statement = foafNames.nextStatement();

                System.out.println(statement.getObject().toString());

            }
        }

        System.out.println();

        StmtIterator foafMails = peopleModel.listStatements(new SimpleSelector(null,
                FOAF.mbox_sha1sum, (RDFNode) null));

        if (foafMails.hasNext()) {
            System.out.println("Printing mails: ");

            while (foafMails.hasNext()) {

                Statement statement = foafMails.nextStatement();

                System.out.println(statement.getObject().toString());

            }
        }

        System.out.println();

        StmtIterator foafLinks = peopleModel.listStatements(new SimpleSelector(null,
                FOAF.homepage, (RDFNode) null));

        if (foafLinks.hasNext()) {
            System.out.println("Printing links: ");

            while (foafLinks.hasNext()) {

                Statement statement = foafLinks.nextStatement();

                System.out.println(statement.getObject().toString());

            }
        }

        System.out.println();

        StmtIterator foafImages = peopleModel.listStatements(new SimpleSelector(null,
                FOAF.img, (RDFNode) null));

        if (foafImages.hasNext()) {
            System.out.println("Printing images: ");

            while (foafImages.hasNext()) {

                Statement statement = foafImages.nextStatement();

                System.out.println(statement.getObject().toString());

            }
        }

    }
}





















//        System.out.println();
//
//        Model peopleModel = ModelFactory.createDefaultModel();
//
//        for (String personURI : peopleURIs) {
//
//            Model personModel = ModelFactory.createDefaultModel();
//
//            StmtIterator personIterator = personModel.listStatements(new SimpleSelector(null, FOAF.mbox_sha1sum,
//                    personURI));
//
//            System.out.println(personIterator.nextStatement());

//            InputStream personInput = FileManager.get().open(personURI);
//
//            if (personInput == null)
//                throw new IllegalArgumentException("File: " + personURI + " not found.");
//
//            personModel.read(personInput, "", "TURTLE");
//
//            peopleModel.union(personModel);

        //peopleModel.write(System.out, "TURTLE");

//
//                Model peopleModel = ModelFactory.createDefaultModel();
//
//                for(String person : peopleIKnow){
//
//                Model personModel = ModelFactory.createDefaultModel();
//
//                InputStream personInput = FileManager.get().open(person);
//
//        if(personInput == null)
//        throw new IllegalArgumentException("File: " + person + " not found.");
//
//        personModel.read(personInput, "", "TURTLE");
//
//        peopleModel.union(personModel);
//        }
//
//        foafProfileModel.union(peopleModel);
//
//        foafProfileModel.write(System.out, "TURTLE");
//        System.out.println();
//
////        foafProfileModel.listStatements(new SimpleSelector(null, FOAF.firstName, (RDFNode) null));
////        foafProfileModel.listStatements(new SimpleSelector(null, FOAF.lastName, (RDFNode) null));
//
////        AKO NE MOZHE PREKU FIRST NAME I LAST NAME, SO NAME PA DELENJE NA PRAZNO MESTO.
//
//        StmtIterator foafNames = foafProfileModel.listStatements(new SimpleSelector(null, FOAF.name, (RDFNode) null));
//
//        if(foafNames.hasNext()){
//        System.out.println("Printing names: ");
//
//        while (foafNames.hasNext()){
//
//        Statement statement = foafNames.nextStatement();
//
//        System.out.println(statement.getObject().toString());
//
//        }
//        }
//
//        StmtIterator foafMails = foafProfileModel.listStatements(new SimpleSelector(null,
//        FOAF.mbox_sha1sum, (RDFNode) null));
//
//        if(foafMails.hasNext()){
//        System.out.println("Printing mails: ");
//
//        while (foafMails.hasNext()){
//
//        Statement statement = foafMails.nextStatement();
//
//        System.out.println(statement.getObject().toString());
//
//        }
//        }
//
//        // LINKS FROM SOCIAL NETWORKS
//        StmtIterator foafLinks = foafProfileModel.listStatements(new SimpleSelector(null,
//        FOAF.homepage, (RDFNode) null));
//
//        if(foafLinks.hasNext()){
//        System.out.println("Printing links: ");
//
//        while (foafLinks.hasNext()){
//
//        Statement statement = foafLinks.nextStatement();
//
//        System.out.println(statement.getObject().toString());
//
//        }
//        }
//
//
//        StmtIterator foafImages = foafProfileModel.listStatements(new SimpleSelector(null,
//        FOAF.img, (RDFNode) null));
////        StmtIterator foafImages = foafProfileModel.listStatements(new SimpleSelector(null,
////                FOAF.thumbnail, (RDFNode) null));
//
////        IF IS NOT WITH FOAF.img, try with FOAF.thumbnail
//
//        if(foafImages.hasNext()){
//        System.out.println("Printing images: ");
//
//        while (foafImages.hasNext()){
//
//        Statement statement = foafImages.nextStatement();
//
//        System.out.println(statement.getObject().toString());
//
//        }
//        }