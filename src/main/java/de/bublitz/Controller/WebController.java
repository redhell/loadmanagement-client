package de.bublitz.Controller;

import de.bublitz.Config.BalancerConfig;
import de.bublitz.Config.ChargeboxConfig;
import de.bublitz.Config.SerialConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

@Controller
@Log4j2
public class WebController {
    @Autowired
    private BalancerConfig balancerConfig;
    @Autowired
    private SerialConfig serialConfig;
    @Autowired
    private ChargeboxConfig chargeboxConfig;

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("chargebox", chargeboxConfig);
        model.addAttribute("serialConfig", serialConfig);
        model.addAttribute("balancer", balancerConfig);
        return "start";
    }

    @PostMapping("/updateCB")
    public RedirectView updateCB(@ModelAttribute ChargeboxConfig chargeboxConfig) {
        this.chargeboxConfig = chargeboxConfig;
        Properties props = new Properties();
        props.setProperty("chargebox.name", chargeboxConfig.getName());
        props.setProperty("chargebox.emaid", chargeboxConfig.getEmaid());
        props.setProperty("chargebox.evseid", chargeboxConfig.getEvseid());
        props.setProperty("chargebox.stopurl", chargeboxConfig.getStopurl());
        props.setProperty("chargebox.starturl", chargeboxConfig.getStarturl());
        writeProps(props, "Chagebox");
        return new RedirectView("");
    }

    @PostMapping("/updateSC")
    public RedirectView updateCB(@ModelAttribute SerialConfig serialConfig) {
        this.serialConfig = serialConfig;
        Properties props = new Properties();
        props.setProperty("serial.port", serialConfig.getPort());
        writeProps(props, "SerialConfig");
        return new RedirectView("");
    }

    @PostMapping("/updateBC")
    public RedirectView updateCB(@ModelAttribute BalancerConfig balancerConfig) {
        this.balancerConfig = balancerConfig;
        Properties props = new Properties();
        props.setProperty("master.port", "" + balancerConfig.getPort());
        props.setProperty("master.ip", balancerConfig.getIp());
        writeProps(props, "BalancerConfig");
        return new RedirectView("");
    }

    private void writeProps(Properties props, String header) {
        try {
            File file = new File("config.properties");
            OutputStream out = new FileOutputStream(file);

            DefaultPropertiesPersister defaultPropertiesPersister
                    = new DefaultPropertiesPersister();
            defaultPropertiesPersister.store(props, out, header);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


}
