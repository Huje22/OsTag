//package me.indian.ostag.form;
//
//
//import cn.nukkit.Player;
//import cn.nukkit.utils.Config;
//import com.formconstructor.form.CustomForm;
//import com.formconstructor.form.ModalForm;
//import com.formconstructor.form.SimpleForm;
//import com.formconstructor.form.element.SelectableElement;
//import com.formconstructor.form.element.custom.Input;
//import com.formconstructor.form.element.custom.Toggle;
//import com.formconstructor.form.element.simple.ImageType;
//import java.util.Arrays;
//import java.util.List;
//import me.indian.ostag.OsTag;
//
//public class ExampleForms {
//
//
//    private final OsTag plugin;
//    private final Player player;
//
//    public ExampleForms(final Player player) {
//        this.player = player;
//        this.plugin = OsTag.getInstance();
//        final Config config = this.plugin.getConfig();
//    }
//
//    public void run(){
//        final SimpleForm form = new SimpleForm("Sample title");
//
//        form.setContent("This is a text")
//                .addContent("\nThis is addition :3")
//                .addButton("Test", ImageType.PATH, "textures/ui/subcategory_icons/head", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/sidebar_icons/profile_screen_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/user_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/book_metatag_hover", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/book_metatag_pressed", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/cartography_table_glass", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/cherry_sign", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/classrooms_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/teams_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/icon_best3", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/icon_bookshelf", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/icon_new", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/lan_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/invite_base", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/lock_color", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/portalBg", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/xbox4", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/store_filter_icon", (p, button) -> run())
//                .addButton("Test", ImageType.PATH, "textures/ui/mute_on", (p, button) -> run())
////                .addButton("Test", ImageType.PATH , "textures/ui/book_metatag_hover" , (p,button) -> p.sendMessage("test"))
//                .addButton("Modal", (p,button) -> modal())
//                .addButton("Custom",(p,button) -> custom());
//
//        form.send(player);
//    }
//
//
//    private void modal(){
//        final ModalForm form = new ModalForm("Test modal form");
//
//        form.setContent("Is OneKN gay?") //local meme in RuNukkitDev
//                .setPositiveButton("Yes")
//                .setNegativeButton("Sure");
//
//        form.setNoneHandler(p -> {
//            run();
//        });
//
//        form.setHandler((p, result) -> {
//            if(result){
//                p.sendMessage("ok");
//            } else {
//                p.sendMessage("ok2");
//            }
//            p.sendMessage(result? "I knew it!" : "Quite right :D");
//        });
//
//        form.send(player);
//    }
//
//    private void custom(){
//        final CustomForm form = new CustomForm("Sample custom form");
//
//        List<SelectableElement> elements = Arrays.asList(
//                new SelectableElement("Option 1"),
//                new SelectableElement("Option 2 but with value", 42),
//                new SelectableElement("Option 3")
//        );
//
//        form.addElement(new Label("This is a test"))
//
//                .addElement("Easy way to add a label")
//
//                .addElement("my-text",   new Input().setName("A sample input"))
//
//                .addElement("my-toggle", new Toggle("Toggle?", true))
//
//                .addElement("my-dd", new Dropdown("Dropdown",  elements))
//
//                .addElement(new Dropdown("Dropdown with default value", elements, 1))
//
//                .addElement("my-ss", new StepSlider("Step slider", elements, 2));
//
//        form.setHandler((p, response) -> {
//            //We can get by id and index
//            p.sendMessage(response.getInput("my-text").getValue());
//
//            p.sendMessage(String.valueOf(response.getToggle("my-toggle").getValue()));
//
//
//            SelectableElement el = response.getDropdown("my-dd").getValue();
//
//            p.sendMessage(el.getText());
//
//            if(el.getValue() != null) p.sendMessage(String.valueOf(el.getValue(Integer.class)));
//
//            el = response.getStepSlider("my-ss").getValue();
//
//            p.sendMessage(el.getText());
//
//        });
//
//        form.setNoneHandler(p -> {
//            run();
//        });
//
//        form.send(player);
//    }
//}