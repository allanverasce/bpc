// package ufpa.biod.pp2oa.dao;

// import java.util.List;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import ufpa.biod.pp2oa.dao.Factory.ParameterFactory;
// import ufpa.biod.pp2oa.model.Parameter;

// public class ParameterDaoTest {
// private ParameterDao parameterDao;
// private Parameter parameter;

// @BeforeEach
// public void setUp() {
// parameterDao = new ParameterDao();
// parameter = ParameterFactory.getParameter();
// new ToolsDao().save(parameter.getTool());
// new ProjectDao().save(parameter.getProject());

// }

// @AfterAll
// public static void suitTearDown() {
// new ParameterDao().findAll().forEach(new ParameterDao()::delete);
// new ToolsDao().findAll().forEach(new ToolsDao()::delete);
// new ProjectDao().findAll().forEach(new ProjectDao()::delete);

// }

// @Test
// public void testSave() {
// parameterDao.save(parameter);
// Assertions.assertNotNull(parameter.getId());

// }

// @Test
// public void update() {
// parameterDao.save(parameter);
// Assertions.assertEquals("Parametro Teste",
// parameterDao.find(parameter.getProject(),
// parameter.getTool()).get(0).getName());
// parameter.setName("Parametro Teste 2");
// parameterDao.save(parameter);
// Assertions.assertEquals("Parametro Teste 2",
// parameterDao.find(parameter.getProject(),
// parameter.getTool()).get(0).getName());

// }

// @Test
// public void testFind() {
// parameterDao.save(parameter);
// Parameter find = parameterDao.find(parameter.getProject(),
// parameter.getTool()).get(0);
// Assertions.assertEquals(parameter, find);

// }

// @Test
// public void testFindById() {
// parameterDao.save(parameter);
// List<Parameter> find = parameterDao.find(parameter.getProject().getId(),
// parameter.getTool().getId());
// Assertions.assertEquals(parameter, find.get(0));

// }

// @Test
// public void testDelete() {
// parameterDao.save(parameter);
// parameterDao.delete(parameter);
// Assertions.assertEquals(0, parameterDao.find(parameter.getProject(),
// parameter.getTool()).size());
// }

// }
