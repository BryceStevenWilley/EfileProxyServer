package edu.suffolk.litlab.efspserver.ecf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.suffolk.litlab.efspserver.SoapX509CallbackHandler;
import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.codes.CodeUpdater;
import edu.suffolk.litlab.efspserver.db.UserDatabase;
import edu.suffolk.litlab.efspserver.services.EfmFilingInterface;
import edu.suffolk.litlab.efspserver.services.EfmModuleSetup;
import edu.suffolk.litlab.efspserver.services.EfmRestCallbackInterface;
import edu.suffolk.litlab.efspserver.services.OrgMessageSender;
import edu.suffolk.litlab.efspserver.services.ServiceHelpers;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingAssemblyMDEPort;

public class TylerModuleSetup implements EfmModuleSetup {
  private static Logger log = 
      LoggerFactory.getLogger(TylerModuleSetup.class); 
  private CodeDatabase cd;
  private String dbUser;
  private String dbPassword;
  private String tylerEndpoint;
  private String x509Password;
  private UserDatabase ud;
  private OrgMessageSender sender;
  private JaxWsServerFactoryBean svrFactory; 
  
  public static class CreationArgs {
    public String dbUser;
    public String dbPassword;
    public String tylerEndpoint;
    public String x509Password;
  }

  /** Use this factory method instead of the class constructor. */
  public static Optional<TylerModuleSetup> create(
      CodeDatabase cd, UserDatabase ud, OrgMessageSender sender) {
    Optional<CreationArgs> args = createFromEnvVars();
    if (args.isEmpty()) {
      return Optional.empty();
    }
    
    return Optional.of(new TylerModuleSetup(args.get(), cd, ud, sender));
  }
  
  private TylerModuleSetup(
      CreationArgs args,
      CodeDatabase cd, UserDatabase ud, OrgMessageSender sender) {
    this.cd = cd; 
    this.dbUser = args.dbUser;
    this.dbPassword = args.dbPassword;
    this.tylerEndpoint = args.tylerEndpoint;
    this.x509Password = args.x509Password;
    this.ud = ud;
    this.sender = sender;
  }
  
  private static Optional<CreationArgs> createFromEnvVars() {
    Optional<String> maybeX509Password = EfmModuleSetup.GetEnv("X509_PASSWORD");
    if (maybeX509Password.isEmpty()) {
      log.warn("If using Tyler, X509_PASSWORD can't be null. Did you forget to source .env?");
      return Optional.empty();
    }
    CreationArgs args = new CreationArgs();
    args.x509Password = maybeX509Password.get();

    Optional<String> maybeTylerEndpoint = EfmModuleSetup.GetEnv("TYLER_ENDPOINT");
    if (maybeTylerEndpoint.isEmpty()) {
      log.warn("If using Tyler, TYLER_ENDPOINT needs to be the defined. Did you forget to source .env?");
      return Optional.empty();
    }
    args.tylerEndpoint = maybeTylerEndpoint.get();

    args.dbUser = EfmModuleSetup.GetEnv("POSTGRES_USER").orElse("postgres"); 
    Optional<String> maybeDbPassword = EfmModuleSetup.GetEnv("POSTGRES_PASSWORD"); 
    if (maybeDbPassword.isEmpty()) {
      log.warn("You need to pass a POSTGRES password in");
      return Optional.empty();
    }
    args.dbPassword = maybeDbPassword.get();
    return Optional.of(args);
  }

  @Override
  public void preSetup() {
    boolean downloadAll = false;
    // HACK(brycew): cheap DI. Should have something better, but
    // I don't quite understand Spring yet
    SoapX509CallbackHandler.setX509Password(x509Password);
    
    try {
      cd.createDbConnection(this.dbUser, this.dbPassword);
      if (cd.getAllLocations().size() == 0) {
        // Code database is empty!
        downloadAll = true;
      }
      if (downloadAll) {
        log.info("Downloading all codes: please wait a bit");
        CodeUpdater.executeCommand("downloadAll", this.tylerEndpoint, cd, this.x509Password);
      }
    } catch (SQLException e) {
      log.error(e.toString());
      throw new RuntimeException(e);
    } catch (JAXBException e) {
      log.error(e.toString());
      throw new RuntimeException(e);
    } catch (IOException e) {
      log.error(e.toString());
      throw new RuntimeException(e);
    } catch (XMLStreamException e) {
      log.error(e.toString());
      throw new RuntimeException(e);
    }
  }

  @Override
  public Set<String> getCourts() {
    // TODO(brycew): we have the setup cd now, should be easy to get all locations.
    Set<String> hardcodedTylerIds = Set.of("adams", "alexander", "bond", "boone", "brown", 
        "bureau", "calhoun", "carroll", "cass", "clay", "cook", 
        "peoria", "perry", "scott", "washington");
    return hardcodedTylerIds;
  }

  @Override
  public EfmFilingInterface getInterface() {
    return new OasisEcfFiler(cd);
  }

  @Override
  public Optional<EfmRestCallbackInterface> getCallback() {
    return Optional.empty();
  }

  @Override
  public void setupGlobals() {
    OasisEcfWsCallback implementor = new OasisEcfWsCallback(ud, sender);
    // TODO(brycew): cleaner way to handle baseLocalUrl?
    String baseLocalUrl = "http://0.0.0.0:9000";
    String address = baseLocalUrl + ServiceHelpers.ASSEMBLY_PORT; 
    svrFactory = new JaxWsServerFactoryBean();
    svrFactory.setServiceClass(FilingAssemblyMDEPort.class);
    svrFactory.setAddress(address);
    svrFactory.setServiceBean(implementor);
    svrFactory.create();
  }

}