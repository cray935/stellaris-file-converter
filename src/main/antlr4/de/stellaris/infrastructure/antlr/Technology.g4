grammar Technology;
@header {
package de.stellaris.infrastructure.antlr;
}

COMMENT: '#' ~( '\r' | '\n' )* [\r\n] -> skip;
NEWLINE: [\n] -> skip;
WORD: [a-zA-Z]+ [a-zA-Z0-9_]* ;
NUMBER: [0-9]+('.'[0-9]+)?;
WS: [ \t\r] -> skip;

technologies 
	: 
	( variable_declaration
	| technology	
	)+
	EOF
;
technology
	: WORD '=' '{'
	( cost
	| area
	| tier
	| category
	| levels
	| cost_per_level
	| prerequisites
	| weight
	| gateway
	| ai_update_type
	| start_tech
	| modifier
	| prereqfor_desc
	| potential
	| weight_modifier
	| weight_groups
	| mod_weight_if_group_picked
	| is_dangerous
	| is_rare
	| ai_weight
	| feature_flags
	| is_reverse_engineerable
	| icon
	)+
	'}'
;
variable_declaration : '@'WORD '=' NUMBER ;
cost : WORD '=' ('@'WORD | NUMBER) ;
area : 'area' '=' ('physics' | 'society' | 'engineering') ;
tier : 'tier' '=' ('@'WORD | NUMBER) ;
category : 'category' '=' '{' WORD '}' ;
levels : 'levels' '=' '-'? NUMBER ;
cost_per_level : 'cost_per_level' '=' ('@'WORD | NUMBER) ;
prerequisites : 'prerequisites' '=' '{' ('"'? WORD '"'?)* '}' ;
weight : 'weight' '=' ('@'WORD | NUMBER) ;
gateway : 'gateway' '=' WORD ;
ai_update_type : 'ai_update_type' '=' WORD ;
start_tech : 'start_tech' '=' ('yes' | 'no') ;
modifier : 'modifier' '=' '{' .*? '}' ;
prereqfor_desc : 'prereqfor_desc' '=' '{' .*? '}' ;
potential : 'potential' '=' '{' .*? '}' ;
weight_modifier : 'weight_modifier' '=' '{' .*? '}' ;
weight_groups : 'weight_groups' '=' '{' .*? '}' ;
mod_weight_if_group_picked : 'mod_weight_if_group_picked' '=' '{' .*? '}' ;
is_dangerous : 'is_dangerous' '=' ('yes' | 'no') ;
is_rare : 'is_rare' '=' ('yes' | 'no') ;
ai_weight : 'ai_weight' '=' '{' .*? '}' ;
feature_flags : 'feature_flags' '=' '{' WORD (',' WORD)*? '}' ;
is_reverse_engineerable : 'is_reverse_engineerable' '=' ('yes' | 'no') ;
icon : 'icon' '=' WORD ;