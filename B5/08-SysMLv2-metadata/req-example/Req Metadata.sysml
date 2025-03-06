library package 'Req Metadata' {
    private import ScalarValues::String;
    private import Metaobjects::SemanticMetadata;

    metadata def StandardRequirement :> SemanticMetadata {
        :>> baseType = StandardRequirement meta SysML::RequirementUsage;

        attribute scope [0..1]: String;
        attribute response: String ;
        attribute trigger [*]: String ;
    }
}
