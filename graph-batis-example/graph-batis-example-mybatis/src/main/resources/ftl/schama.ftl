type ${gqs.name} {
<#list gqs.fields as field>
    ${field.name}: ${field.type.name}
</#list>
}