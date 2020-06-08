type ${name} {
<#list fields as field>
 ${field.name}: ${field.type.name}
</#list>
}