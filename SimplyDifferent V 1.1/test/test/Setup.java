package test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"Setup-context.xml","file:///E:/workspace_latest/SimplyDifferent V 1.1/WebContent/WEB-INF/applicationContext.xml"}) 
@ActiveProfiles(profiles={"testing"})
public class Setup { 

}
