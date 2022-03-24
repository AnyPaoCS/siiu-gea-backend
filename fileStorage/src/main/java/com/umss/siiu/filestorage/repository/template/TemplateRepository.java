/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.repository.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umss.siiu.core.model.ModelBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateRepository {

    private static final String CLASS = "class";

    private static Logger logger = LoggerFactory.getLogger("TemplateRepository");

    /*private static CellMappings cellMappings;

    private static CellMappings titleMappings;

    private static CellMappingParser parser;

    private static ExcelWriter excelWriter;*/

    private final ObjectMapper objectMapper;


    @Autowired
    public TemplateRepository(/*CellMappingParser parser, ExcelWriter excelWriter,*/ ObjectMapper objectMapper) {
        /*this.parser = parser;
        this.excelWriter = excelWriter;*/
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    public void initialize() {
        try {
            cellMappings = objectMapper.readValue(TypeReference.class.getResourceAsStream(cellMappingsFile),
                    CellMappings.class);
            titleMappings = objectMapper.readValue(TypeReference.class.getResourceAsStream(titleAliasFile),
                    CellMappings.class);
        } catch (Exception e) {
            exceptionHandler(e, "");
        }
    }*/

    /*public <T extends ModelBase<?>> OutputStream save(T model, InputStream stream) {
        return processMapping(model, stream, "", "");
    }*/

/*
    public <T extends ModelBase<?>> OutputStream save(T model, InputStream stream, String fileTypeCode, String args) {
        return processMapping(model, stream, fileTypeCode, args);
    }
*/

/*
    @Deprecated
    private <T extends ModelBase<?>> OutputStream processMapping(T model, InputStream stream, String fileTypeCode,
                                                                 String args) {
        EntityMapping entryMap = fetchEntrySet(model.getClass().getCanonicalName(), fileTypeCode,
                cellMappings.getTemplateMappings().entrySet(),
                fetchDefaultTemplateMaping(cellMappings.getTemplateMappings().entrySet()));
        if (entryMap != null) {
            return excelWriter.writeValuesIntoTemplate(createCellMappings(model, entryMap, args, ""), stream);
        }
        throw new IllegalArgumentException(String.format("No mapping for %s", model.getClass().getCanonicalName()));
    }
*/

    /*public <T extends ModelBase<?>> void save(T model, Workbook workbook, String fileTypeCode, String args) {
        processMapping(model, workbook, fileTypeCode, args, "");
    }

    public <T extends ModelBase<?>> void save(T model, Workbook workbook, String fileTypeCode, String args,
                                              String sheetName) {
        processMapping(model, workbook, fileTypeCode, args, sheetName);
    }*/

/*
    private Entry<String, TemplateMapping> fetchDefaultTemplateMaping(Set<Entry<String, TemplateMapping>> entries) {
        return entries.stream().filter(entry -> entry.getKey().equals("default")).findFirst()
                .orElseThrow(() -> new IllegalStateException("No default mappings were found!"));
    }
*/

/*
    private <T extends ModelBase<?>> void processMapping(T model, Workbook workbook, String fileTypeCode, String args,
                                                         String sheetName) {
        EntityMapping entryMap = fetchEntrySet(model.getClass().getCanonicalName(), fileTypeCode,
                cellMappings.getTemplateMappings().entrySet(),
                fetchDefaultTemplateMaping(cellMappings.getTemplateMappings().entrySet()));
        if (entryMap != null) {
            processModel(model, workbook, fileTypeCode, args, sheetName, entryMap);
        } else {
            throw new IllegalArgumentException(String.format("No mapping for %s", model.getClass().getCanonicalName()));
        }
    }
*/

/*
    private EntityMapping fetchEntrySet(String caninicalName, String fileEntryKey,
                                        Set<Entry<String, TemplateMapping>> entries,
                                        Entry<String, TemplateMapping> defaultMapping) {
        TemplateMapping templateMapping = entries.stream().filter(entry -> entry.getKey().equals(fileEntryKey))
                .findFirst().orElse(defaultMapping).getValue();
        return fetchCellByNameAndTemplate(caninicalName, templateMapping);
    }
*/

/*
    private EntityMapping fetchCellByNameAndTemplate(String caninicalName, TemplateMapping templateMapping) {
        if (templateMapping.getEntityMappings().containsKey(caninicalName)) {
            return templateMapping.getEntityMappings().get(caninicalName);
        }
        return null;
    }
*/

/*
    private <T extends ModelBase<?>> void processModel(T model, Workbook workbook, String fileTypeCode, String args,
                                                       String sheetName, EntityMapping entryMap) {
            excelWriter.writeValuesIntoTemplate(createCellMappings(model, entryMap, args, sheetName), workbook);
    }
*/

/*
    private static <T extends ModelBase<?>> List<CellProperties> createCellMappings(T model, EntityMapping entryMap,
                                                                                    String args, String sheetname) {
        try {
            List<CellProperties> cellMappings = new ArrayList<>();
            Map<String, PropertyDescriptor> attributes = mapAttributesOf(model);
            for (Entry<String, String> entry : entryMap.getEntries().entrySet()) {
                PropertyDescriptor attribute = attributes.get(entry.getKey());
                if (attribute != null) {
                    String cell = entry.getValue();
                    if (null != sheetname && !sheetname.equals("")) {
                        cell = String.format(entry.getValue(), sheetname);
                    }
                    appendValuesWithExtraValidation(model, args, cellMappings, entry, attribute, cell);
                } else {
                    throw new IllegalStateException(String.format(
                            "The model does not have a correspondent for the field mapping %s", entry.getKey()));
                }
            }
            return cellMappings;
        } catch (Exception e) {
            return reportError(model, e);
        }
    }
*/

/*
    private static void appendValuesWithExtraValidation(ModelBase<?> model, String args,
                                                        List<CellProperties> cellMappings,
                                                        Entry<String, String> entry, PropertyDescriptor attribute,
                                                        String cell)
            throws IllegalAccessException, InvocationTargetException {
            appendValues(cellMappings, entry.getKey(),
                    new CellProperties(parser.parse(cell, args), attribute.getReadMethod().invoke(model), false));
    }
*/

/*
    private static void appendValues(List<CellProperties> cellMappings, String fieldName,
                                     CellProperties cellProperties) {
        cellMappings.add(cellProperties);
        String msg = String.format("%s:%s cell %s", fieldName, cellProperties.getValue(), cellProperties.getName());
        logger.info(msg);
    }
*/

    private static <T extends ModelBase<?>> Map<String, PropertyDescriptor> mapAttributesOf(T model)
            throws IntrospectionException {
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(model.getClass()).getPropertyDescriptors()) {
            if (pd.getReadMethod() != null && !CLASS.equals(pd.getName()))
                propertyDescriptorMap.put(pd.getName(), pd);
        }
        return propertyDescriptorMap;
    }

/*
    private static List<CellProperties> reportError(ModelBase<?> model, Exception e) {
        String canonicalName = model.getClass().getCanonicalName();
        exceptionHandler(e, canonicalName);
        return new ArrayList<>();
    }
*/

/*
    private static <E extends Exception> void exceptionHandler(E exception, String arg) {
        if (exception instanceof IntrospectionException) {
            throw new IllegalStateException(String.format("There was an introspection error analyzing %s", arg),
                    exception);
        }
        if (exception instanceof IllegalAccessException) {
            throw new IllegalStateException(
                    String.format("There was an method access error accessing a property accessor of %s", arg),
                    exception);
        }
        if (exception instanceof InvocationTargetException) {
            throw new IllegalStateException(
                    String.format("There was an method access error accessing a property accessor of %s", arg),
                    exception);
        }
        if (exception instanceof FileNotFoundException) {
            throw new IllegalStateException("The mapping file was not found!", exception);
        }
        if (exception instanceof JsonGenerationException || exception instanceof JsonMappingException) {
            throw new IllegalStateException("There was an error generating json from the mapping file.", exception);
        }
        if (exception instanceof IOException) {
            throw new IllegalStateException("There was an error accessing the mapping file", exception);
        }
        if (exception instanceof IllegalStateException) {
            throw new IllegalStateException("Business Rule error has been triggered", exception);
        }
        throw new IllegalStateException("Unknown Exception triggered!", exception);
    }
*/
}
