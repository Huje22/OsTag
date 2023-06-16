package me.indian.ostag.from;


import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PluginInfoUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.ModalForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.contentforge.formconstructor.form.element.StepSlider;
import ru.contentforge.formconstructor.form.element.Toggle;


import java.util.Arrays;
import java.util.List;

public class ExampleForms {


    private final OsTag plugin;
    private final Player player;

    public ExampleForms(final Player player) {
        this.player = player;
        this.plugin = OsTag.getInstance();
        final Config config = this.plugin.getConfig();
    }

    public void run(){
        final SimpleForm form = new SimpleForm("Sample title");

        form.setContent("This is a text")
                .addContent("\nThis is addition :3")
                .addButton("Modal", (p,button) -> modal())
                .addButton("Custom",(p,button) -> custom());

        form.send(player);
    }


    private void modal(){
        final ModalForm form = new ModalForm("Test modal form");

        form.setContent("Is OneKN gay?") //local meme in RuNukkitDev
                .setPositiveButton("Yes")
                .setNegativeButton("Sure");

        form.setNoneHandler(p -> {
            run();
        });

        form.setHandler((p, result) -> {
            if(result){
                p.sendMessage("ok");
            } else {
                p.sendMessage("ok2");
            }
            p.sendMessage(result? "I knew it!" : "Quite right :D");
        });

        form.send(player);
    }

    private void custom(){
        final CustomForm form = new CustomForm("Sample custom form");

        List<SelectableElement> elements = Arrays.asList(
                new SelectableElement("Option 1"),
                new SelectableElement("Option 2 but with value", 42),
                new SelectableElement("Option 3")
        );

        form.addElement(new Label("This is a test"))

                .addElement("Easy way to add a label")

                .addElement("my-text", Input.builder().setName("A sample input").build())

                .addElement("my-toggle", new Toggle("Toggle?", true))

                .addElement("my-dd", new Dropdown("Dropdown",  elements))

                .addElement(new Dropdown("Dropdown with default value", elements, 1))

                .addElement("my-ss", new StepSlider("Step slider", elements, 2));

        form.setHandler((p, response) -> {
            //We can get by id and index
            p.sendMessage(response.getInput("my-text").getValue());

            p.sendMessage(String.valueOf(response.getToggle("my-toggle").getValue()));


            SelectableElement el = response.getDropdown("my-dd").getValue();

            p.sendMessage(el.getText());

            if(el.getValue() != null) p.sendMessage(String.valueOf(el.getValue(Integer.class)));

            el = response.getStepSlider("my-ss").getValue();

            p.sendMessage(el.getText());

        });

        form.setNoneHandler(p -> {
            run();
        });

        form.send(player);
    }
}