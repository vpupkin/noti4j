// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecurityConfigurationXpp3Reader.java

package eu.blky.log4j.Gdata;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
 
public class SecurityConfigurationXpp3Reader
{

    public SecurityConfigurationXpp3Reader()
    {
        addDefaultEntities = true;
    }

    private boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, Set parsed)
        throws XmlPullParserException
    {
        if(!parser.getName().equals(tagName) && !parser.getName().equals(alias))
            return false;
        if(parsed.contains(tagName))
        {
            throw new XmlPullParserException("Duplicated tag: '" + tagName + "'", parser, null);
        } else
        {
            parsed.add(tagName);
            return true;
        }
    }

    public boolean getAddDefaultEntities()
    {
        return addDefaultEntities;
    }

    private boolean getBooleanValue(String s, String attribute, XmlPullParser parser)
        throws XmlPullParserException
    {
        return getBooleanValue(s, attribute, parser, null);
    }

    private boolean getBooleanValue(String s, String attribute, XmlPullParser parser, String defaultValue)
        throws XmlPullParserException
    {
        if(s != null && s.length() != 0)
            return Boolean.valueOf(s).booleanValue();
        if(defaultValue != null)
            return Boolean.valueOf(defaultValue).booleanValue();
        else
            return false;
    }

    private byte getByteValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Byte.valueOf(s).byteValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a byte", parser, null);
        }
        return 0;
    }

    private char getCharacterValue(String s, String attribute, XmlPullParser parser)
        throws XmlPullParserException
    {
        if(s != null)
            return s.charAt(0);
        else
            return '\0';
    }

    private Date getDateValue(String s, String attribute, XmlPullParser parser)
        throws XmlPullParserException
    {
        return getDateValue(s, attribute, null, parser);
    }

    private Date getDateValue(String s, String attribute, String dateFormat, XmlPullParser parser)
        throws XmlPullParserException
    {
        if(s != null)
        {
            String effectiveDateFormat = dateFormat;
            if(dateFormat == null)
                effectiveDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            if("long".equals(effectiveDateFormat))
                try
                {
                    return new Date(Long.parseLong(s));
                }
                catch(NumberFormatException e)
                {
                    throw new XmlPullParserException(e.getMessage());
                }
            try
            {
                DateFormat dateParser = new SimpleDateFormat(effectiveDateFormat, Locale.US);
                return dateParser.parse(s);
            }
            catch(ParseException e)
            {
                throw new XmlPullParserException(e.getMessage());
            }
        } else
        {
            return null;
        }
    }

    private double getDoubleValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Double.valueOf(s).doubleValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, null);
        }
        return 0.0D;
    }

    private float getFloatValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Float.valueOf(s).floatValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, null);
        }
        return 0.0F;
    }

    private int getIntegerValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Integer.valueOf(s).intValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be an integer", parser, null);
        }
        return 0;
    }

    private long getLongValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Long.valueOf(s).longValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a long integer", parser, null);
        }
        return 0L;
    }

    private String getRequiredAttributeValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s == null && strict)
            throw new XmlPullParserException("Missing required value for attribute '" + attribute + "'", parser, null);
        else
            return s;
    }

    private short getShortValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if(s != null)
        {
            try
            {
                return Short.valueOf(s).shortValue();
            }
            catch(NumberFormatException e) { }
            if(strict)
                throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a short integer", parser, null);
        }
        return 0;
    }

    private String getTrimmedValue(String s)
    {
        if(s != null)
            s = s.trim();
        return s;
    }

    private Config parseConfig(String tagName, XmlPullParser parser, boolean strict)
        throws IOException, XmlPullParserException
    {
        Config config = new Config();
        Set parsed = new HashSet();
        while(parser.nextTag() == 2) 
            if(checkFieldWithDuplicate(parser, "name", null, parsed))
                config.setName(getTrimmedValue(parser.nextText()));
            else
            if(checkFieldWithDuplicate(parser, "properties", null, parsed))
            {
                List properties = new ArrayList();
                config.setProperties(properties);
                while(parser.nextTag() == 2) 
                    if(parser.getName().equals("property"))
                    {
                        properties.add(parseConfigProperty("property", parser, strict));
                    } else
                    {
                        if(strict)
                            throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, null);
                        while(parser.next() != 3) ;
                    }
            } else
            {
                if(strict)
                    throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, null);
                while(parser.next() != 3) ;
            }
        return config;
    }

    private ConfigProperty parseConfigProperty(String tagName, XmlPullParser parser, boolean strict)
        throws IOException, XmlPullParserException
    {
        ConfigProperty configProperty = new ConfigProperty();
        Set parsed = new HashSet();
        while(parser.nextTag() == 2) 
            if(checkFieldWithDuplicate(parser, "name", null, parsed))
                configProperty.setName(getTrimmedValue(parser.nextText()));
            else
            if(checkFieldWithDuplicate(parser, "value", null, parsed))
            {
                configProperty.setValue(getTrimmedValue(parser.nextText()));
            } else
            {
                if(strict)
                    throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, null);
                while(parser.next() != 3) ;
            }
        return configProperty;
    }

    private SettingsSecurity parseSettingsSecurity(String tagName, XmlPullParser parser, boolean strict)
        throws IOException, XmlPullParserException
    {
        SettingsSecurity settingsSecurity = new SettingsSecurity();
        Set parsed = new HashSet();
        int eventType = parser.getEventType();
        boolean foundRoot = false;
label0:
        for(; eventType != 1; eventType = parser.next())
        {
            if(eventType != 2)
                continue;
            if(parser.getName().equals(tagName))
            {
                foundRoot = true;
                continue;
            }
            if(strict && !foundRoot)
                throw new XmlPullParserException("Expected root element '" + tagName + "' but found '" + parser.getName() + "'", parser, null);
            if(checkFieldWithDuplicate(parser, "master", null, parsed))
            {
                settingsSecurity.setMaster(getTrimmedValue(parser.nextText()));
                continue;
            }
            if(checkFieldWithDuplicate(parser, "relocation", null, parsed))
            {
                settingsSecurity.setRelocation(getTrimmedValue(parser.nextText()));
                continue;
            }
            if(checkFieldWithDuplicate(parser, "configurations", null, parsed))
            {
                List configurations = new ArrayList();
                settingsSecurity.setConfigurations(configurations);
                do
                {
                    do
                    {
                        if(parser.nextTag() != 2)
                            continue label0;
                        if(!parser.getName().equals("configuration"))
                            break;
                        configurations.add(parseConfig("configuration", parser, strict));
                    } while(true);
                    if(strict)
                        throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, null);
                    while(parser.next() != 3) ;
                } while(true);
            }
            if(strict)
                throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, null);
        }

        return settingsSecurity;
    }

    public SettingsSecurity read(Reader reader, boolean strict)
        throws IOException, XmlPullParserException
    {
        XmlPullParser parser = new /*hidden.org.codehaus.plexus.util.xml.pull.*/MXParser();
        parser.setInput(reader);
        if(addDefaultEntities)
        {
            parser.defineEntityReplacementText("nbsp", "\240");
            parser.defineEntityReplacementText("iexcl", "\241");
            parser.defineEntityReplacementText("cent", "\242");
            parser.defineEntityReplacementText("pound", "\243");
            parser.defineEntityReplacementText("curren", "\244");
            parser.defineEntityReplacementText("yen", "\245");
            parser.defineEntityReplacementText("brvbar", "\246");
            parser.defineEntityReplacementText("sect", "\247");
            parser.defineEntityReplacementText("uml", "\250");
            parser.defineEntityReplacementText("copy", "\251");
            parser.defineEntityReplacementText("ordf", "\252");
            parser.defineEntityReplacementText("laquo", "\253");
            parser.defineEntityReplacementText("not", "\254");
            parser.defineEntityReplacementText("shy", "\255");
            parser.defineEntityReplacementText("reg", "\256");
            parser.defineEntityReplacementText("macr", "\257");
            parser.defineEntityReplacementText("deg", "\260");
            parser.defineEntityReplacementText("plusmn", "\261");
            parser.defineEntityReplacementText("sup2", "\262");
            parser.defineEntityReplacementText("sup3", "\263");
            parser.defineEntityReplacementText("acute", "\264");
            parser.defineEntityReplacementText("micro", "\265");
            parser.defineEntityReplacementText("para", "\266");
            parser.defineEntityReplacementText("middot", "\267");
            parser.defineEntityReplacementText("cedil", "\270");
            parser.defineEntityReplacementText("sup1", "\271");
            parser.defineEntityReplacementText("ordm", "\272");
            parser.defineEntityReplacementText("raquo", "\273");
            parser.defineEntityReplacementText("frac14", "\274");
            parser.defineEntityReplacementText("frac12", "\275");
            parser.defineEntityReplacementText("frac34", "\276");
            parser.defineEntityReplacementText("iquest", "\277");
            parser.defineEntityReplacementText("Agrave", "\300");
            parser.defineEntityReplacementText("Aacute", "\301");
            parser.defineEntityReplacementText("Acirc", "\302");
            parser.defineEntityReplacementText("Atilde", "\303");
            parser.defineEntityReplacementText("Auml", "\304");
            parser.defineEntityReplacementText("Aring", "\305");
            parser.defineEntityReplacementText("AElig", "\306");
            parser.defineEntityReplacementText("Ccedil", "\307");
            parser.defineEntityReplacementText("Egrave", "\310");
            parser.defineEntityReplacementText("Eacute", "\311");
            parser.defineEntityReplacementText("Ecirc", "\312");
            parser.defineEntityReplacementText("Euml", "\313");
            parser.defineEntityReplacementText("Igrave", "\314");
            parser.defineEntityReplacementText("Iacute", "\315");
            parser.defineEntityReplacementText("Icirc", "\316");
            parser.defineEntityReplacementText("Iuml", "\317");
            parser.defineEntityReplacementText("ETH", "\320");
            parser.defineEntityReplacementText("Ntilde", "\321");
            parser.defineEntityReplacementText("Ograve", "\322");
            parser.defineEntityReplacementText("Oacute", "\323");
            parser.defineEntityReplacementText("Ocirc", "\324");
            parser.defineEntityReplacementText("Otilde", "\325");
            parser.defineEntityReplacementText("Ouml", "\326");
            parser.defineEntityReplacementText("times", "\327");
            parser.defineEntityReplacementText("Oslash", "\330");
            parser.defineEntityReplacementText("Ugrave", "\331");
            parser.defineEntityReplacementText("Uacute", "\332");
            parser.defineEntityReplacementText("Ucirc", "\333");
            parser.defineEntityReplacementText("Uuml", "\334");
            parser.defineEntityReplacementText("Yacute", "\335");
            parser.defineEntityReplacementText("THORN", "\336");
            parser.defineEntityReplacementText("szlig", "\337");
            parser.defineEntityReplacementText("agrave", "\340");
            parser.defineEntityReplacementText("aacute", "\341");
            parser.defineEntityReplacementText("acirc", "\342");
            parser.defineEntityReplacementText("atilde", "\343");
            parser.defineEntityReplacementText("auml", "\344");
            parser.defineEntityReplacementText("aring", "\345");
            parser.defineEntityReplacementText("aelig", "\346");
            parser.defineEntityReplacementText("ccedil", "\347");
            parser.defineEntityReplacementText("egrave", "\350");
            parser.defineEntityReplacementText("eacute", "\351");
            parser.defineEntityReplacementText("ecirc", "\352");
            parser.defineEntityReplacementText("euml", "\353");
            parser.defineEntityReplacementText("igrave", "\354");
            parser.defineEntityReplacementText("iacute", "\355");
            parser.defineEntityReplacementText("icirc", "\356");
            parser.defineEntityReplacementText("iuml", "\357");
            parser.defineEntityReplacementText("eth", "\360");
            parser.defineEntityReplacementText("ntilde", "\361");
            parser.defineEntityReplacementText("ograve", "\362");
            parser.defineEntityReplacementText("oacute", "\363");
            parser.defineEntityReplacementText("ocirc", "\364");
            parser.defineEntityReplacementText("otilde", "\365");
            parser.defineEntityReplacementText("ouml", "\366");
            parser.defineEntityReplacementText("divide", "\367");
            parser.defineEntityReplacementText("oslash", "\370");
            parser.defineEntityReplacementText("ugrave", "\371");
            parser.defineEntityReplacementText("uacute", "\372");
            parser.defineEntityReplacementText("ucirc", "\373");
            parser.defineEntityReplacementText("uuml", "\374");
            parser.defineEntityReplacementText("yacute", "\375");
            parser.defineEntityReplacementText("thorn", "\376");
            parser.defineEntityReplacementText("yuml", "\377");
            parser.defineEntityReplacementText("OElig", "\u0152");
            parser.defineEntityReplacementText("oelig", "\u0153");
            parser.defineEntityReplacementText("Scaron", "\u0160");
            parser.defineEntityReplacementText("scaron", "\u0161");
            parser.defineEntityReplacementText("Yuml", "\u0178");
            parser.defineEntityReplacementText("circ", "\u02C6");
            parser.defineEntityReplacementText("tilde", "\u02DC");
            parser.defineEntityReplacementText("ensp", "\u2002");
            parser.defineEntityReplacementText("emsp", "\u2003");
            parser.defineEntityReplacementText("thinsp", "\u2009");
            parser.defineEntityReplacementText("zwnj", "\u200C");
            parser.defineEntityReplacementText("zwj", "\u200D");
            parser.defineEntityReplacementText("lrm", "\u200E");
            parser.defineEntityReplacementText("rlm", "\u200F");
            parser.defineEntityReplacementText("ndash", "\u2013");
            parser.defineEntityReplacementText("mdash", "\u2014");
            parser.defineEntityReplacementText("lsquo", "\u2018");
            parser.defineEntityReplacementText("rsquo", "\u2019");
            parser.defineEntityReplacementText("sbquo", "\u201A");
            parser.defineEntityReplacementText("ldquo", "\u201C");
            parser.defineEntityReplacementText("rdquo", "\u201D");
            parser.defineEntityReplacementText("bdquo", "\u201E");
            parser.defineEntityReplacementText("dagger", "\u2020");
            parser.defineEntityReplacementText("Dagger", "\u2021");
            parser.defineEntityReplacementText("permil", "\u2030");
            parser.defineEntityReplacementText("lsaquo", "\u2039");
            parser.defineEntityReplacementText("rsaquo", "\u203A");
            parser.defineEntityReplacementText("euro", "\u20AC");
            parser.defineEntityReplacementText("fnof", "\u0192");
            parser.defineEntityReplacementText("Alpha", "\u0391");
            parser.defineEntityReplacementText("Beta", "\u0392");
            parser.defineEntityReplacementText("Gamma", "\u0393");
            parser.defineEntityReplacementText("Delta", "\u0394");
            parser.defineEntityReplacementText("Epsilon", "\u0395");
            parser.defineEntityReplacementText("Zeta", "\u0396");
            parser.defineEntityReplacementText("Eta", "\u0397");
            parser.defineEntityReplacementText("Theta", "\u0398");
            parser.defineEntityReplacementText("Iota", "\u0399");
            parser.defineEntityReplacementText("Kappa", "\u039A");
            parser.defineEntityReplacementText("Lambda", "\u039B");
            parser.defineEntityReplacementText("Mu", "\u039C");
            parser.defineEntityReplacementText("Nu", "\u039D");
            parser.defineEntityReplacementText("Xi", "\u039E");
            parser.defineEntityReplacementText("Omicron", "\u039F");
            parser.defineEntityReplacementText("Pi", "\u03A0");
            parser.defineEntityReplacementText("Rho", "\u03A1");
            parser.defineEntityReplacementText("Sigma", "\u03A3");
            parser.defineEntityReplacementText("Tau", "\u03A4");
            parser.defineEntityReplacementText("Upsilon", "\u03A5");
            parser.defineEntityReplacementText("Phi", "\u03A6");
            parser.defineEntityReplacementText("Chi", "\u03A7");
            parser.defineEntityReplacementText("Psi", "\u03A8");
            parser.defineEntityReplacementText("Omega", "\u03A9");
            parser.defineEntityReplacementText("alpha", "\u03B1");
            parser.defineEntityReplacementText("beta", "\u03B2");
            parser.defineEntityReplacementText("gamma", "\u03B3");
            parser.defineEntityReplacementText("delta", "\u03B4");
            parser.defineEntityReplacementText("epsilon", "\u03B5");
            parser.defineEntityReplacementText("zeta", "\u03B6");
            parser.defineEntityReplacementText("eta", "\u03B7");
            parser.defineEntityReplacementText("theta", "\u03B8");
            parser.defineEntityReplacementText("iota", "\u03B9");
            parser.defineEntityReplacementText("kappa", "\u03BA");
            parser.defineEntityReplacementText("lambda", "\u03BB");
            parser.defineEntityReplacementText("mu", "\u03BC");
            parser.defineEntityReplacementText("nu", "\u03BD");
            parser.defineEntityReplacementText("xi", "\u03BE");
            parser.defineEntityReplacementText("omicron", "\u03BF");
            parser.defineEntityReplacementText("pi", "\u03C0");
            parser.defineEntityReplacementText("rho", "\u03C1");
            parser.defineEntityReplacementText("sigmaf", "\u03C2");
            parser.defineEntityReplacementText("sigma", "\u03C3");
            parser.defineEntityReplacementText("tau", "\u03C4");
            parser.defineEntityReplacementText("upsilon", "\u03C5");
            parser.defineEntityReplacementText("phi", "\u03C6");
            parser.defineEntityReplacementText("chi", "\u03C7");
            parser.defineEntityReplacementText("psi", "\u03C8");
            parser.defineEntityReplacementText("omega", "\u03C9");
            parser.defineEntityReplacementText("thetasym", "\u03D1");
            parser.defineEntityReplacementText("upsih", "\u03D2");
            parser.defineEntityReplacementText("piv", "\u03D6");
            parser.defineEntityReplacementText("bull", "\u2022");
            parser.defineEntityReplacementText("hellip", "\u2026");
            parser.defineEntityReplacementText("prime", "\u2032");
            parser.defineEntityReplacementText("Prime", "\u2033");
            parser.defineEntityReplacementText("oline", "\u203E");
            parser.defineEntityReplacementText("frasl", "\u2044");
            parser.defineEntityReplacementText("weierp", "\u2118");
            parser.defineEntityReplacementText("image", "\u2111");
            parser.defineEntityReplacementText("real", "\u211C");
            parser.defineEntityReplacementText("trade", "\u2122");
            parser.defineEntityReplacementText("alefsym", "\u2135");
            parser.defineEntityReplacementText("larr", "\u2190");
            parser.defineEntityReplacementText("uarr", "\u2191");
            parser.defineEntityReplacementText("rarr", "\u2192");
            parser.defineEntityReplacementText("darr", "\u2193");
            parser.defineEntityReplacementText("harr", "\u2194");
            parser.defineEntityReplacementText("crarr", "\u21B5");
            parser.defineEntityReplacementText("lArr", "\u21D0");
            parser.defineEntityReplacementText("uArr", "\u21D1");
            parser.defineEntityReplacementText("rArr", "\u21D2");
            parser.defineEntityReplacementText("dArr", "\u21D3");
            parser.defineEntityReplacementText("hArr", "\u21D4");
            parser.defineEntityReplacementText("forall", "\u2200");
            parser.defineEntityReplacementText("part", "\u2202");
            parser.defineEntityReplacementText("exist", "\u2203");
            parser.defineEntityReplacementText("empty", "\u2205");
            parser.defineEntityReplacementText("nabla", "\u2207");
            parser.defineEntityReplacementText("isin", "\u2208");
            parser.defineEntityReplacementText("notin", "\u2209");
            parser.defineEntityReplacementText("ni", "\u220B");
            parser.defineEntityReplacementText("prod", "\u220F");
            parser.defineEntityReplacementText("sum", "\u2211");
            parser.defineEntityReplacementText("minus", "\u2212");
            parser.defineEntityReplacementText("lowast", "\u2217");
            parser.defineEntityReplacementText("radic", "\u221A");
            parser.defineEntityReplacementText("prop", "\u221D");
            parser.defineEntityReplacementText("infin", "\u221E");
            parser.defineEntityReplacementText("ang", "\u2220");
            parser.defineEntityReplacementText("and", "\u2227");
            parser.defineEntityReplacementText("or", "\u2228");
            parser.defineEntityReplacementText("cap", "\u2229");
            parser.defineEntityReplacementText("cup", "\u222A");
            parser.defineEntityReplacementText("int", "\u222B");
            parser.defineEntityReplacementText("there4", "\u2234");
            parser.defineEntityReplacementText("sim", "\u223C");
            parser.defineEntityReplacementText("cong", "\u2245");
            parser.defineEntityReplacementText("asymp", "\u2248");
            parser.defineEntityReplacementText("ne", "\u2260");
            parser.defineEntityReplacementText("equiv", "\u2261");
            parser.defineEntityReplacementText("le", "\u2264");
            parser.defineEntityReplacementText("ge", "\u2265");
            parser.defineEntityReplacementText("sub", "\u2282");
            parser.defineEntityReplacementText("sup", "\u2283");
            parser.defineEntityReplacementText("nsub", "\u2284");
            parser.defineEntityReplacementText("sube", "\u2286");
            parser.defineEntityReplacementText("supe", "\u2287");
            parser.defineEntityReplacementText("oplus", "\u2295");
            parser.defineEntityReplacementText("otimes", "\u2297");
            parser.defineEntityReplacementText("perp", "\u22A5");
            parser.defineEntityReplacementText("sdot", "\u22C5");
            parser.defineEntityReplacementText("lceil", "\u2308");
            parser.defineEntityReplacementText("rceil", "\u2309");
            parser.defineEntityReplacementText("lfloor", "\u230A");
            parser.defineEntityReplacementText("rfloor", "\u230B");
            parser.defineEntityReplacementText("lang", "\u2329");
            parser.defineEntityReplacementText("rang", "\u232A");
            parser.defineEntityReplacementText("loz", "\u25CA");
            parser.defineEntityReplacementText("spades", "\u2660");
            parser.defineEntityReplacementText("clubs", "\u2663");
            parser.defineEntityReplacementText("hearts", "\u2665");
            parser.defineEntityReplacementText("diams", "\u2666");
        }
        parser.next();
        return parseSettingsSecurity("settingsSecurity", parser, strict);
    }

    public SettingsSecurity read(Reader reader)
        throws IOException, XmlPullParserException
    {
        return read(reader, true);
    }

    public SettingsSecurity read(InputStream in, boolean strict)
        throws IOException, XmlPullParserException
    {
        Reader reader = /*hidden.org.codehaus.plexus.util.ReaderFactory.*/newXmlReader(in);
        return read(reader, strict);
    }

    private Reader newXmlReader(InputStream in) throws IOException {
		return ReaderFactory.newXmlReader(in);
	}

	public SettingsSecurity read(InputStream in)
        throws IOException, XmlPullParserException
    {
        Reader reader = /*hidden.org.codehaus.plexus.util.ReaderFactory.*/newXmlReader(in);
        return read(reader);
    }

    public void setAddDefaultEntities(boolean addDefaultEntities)
    {
        this.addDefaultEntities = addDefaultEntities;
    }

    private boolean addDefaultEntities;
}
