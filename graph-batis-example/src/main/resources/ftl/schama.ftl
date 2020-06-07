type ${o.name} {
<#list o.fileds as field>
${field.name}: ${field.type}
</#list>
}