package com.fingy.robocall.util;

import com.fingy.robocall.web.controller.dto.Response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class JAXBUtil {

    public static <T> String marshallToString(T response) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();
        jaxbMarshaller.marshal(response, writer);
        return writer.toString();

    }
}
