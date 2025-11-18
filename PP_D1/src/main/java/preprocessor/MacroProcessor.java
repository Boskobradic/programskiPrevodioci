package preprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacroProcessor {

    private String sourceCode;

    public MacroProcessor(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String process() {
        Map<String, String> macros = findAndRemoveMacroDefinitions();
        return applyMacros(macros);
    }

    private Map<String, String> findAndRemoveMacroDefinitions() {
        Map<String, String> macros = new HashMap<>();

        Pattern pattern = Pattern.compile("defmacro\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+(.*?);", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(this.sourceCode);

        StringBuilder newSourceCode = new StringBuilder();

        while (matcher.find()) {
            String macroName = matcher.group(1);
            String macroBody = matcher.group(2).trim();

            if (macroName != null && macroBody != null) {
                macros.put(macroName, macroBody);
            }
            matcher.appendReplacement(newSourceCode, "");
        }
        matcher.appendTail(newSourceCode);

        this.sourceCode = newSourceCode.toString();
        return macros;
    }

    private String applyMacros(Map<String, String> macros) {
        String result = this.sourceCode;
        for (Map.Entry<String, String> entry : macros.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}