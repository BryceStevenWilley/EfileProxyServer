
package oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AcceptedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AdValoremIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AnimalFoodApprovedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AnimalFoodIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AuctionConstraintIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BackOrderAllowedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BalanceBroughtForwardIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BasedOnConsensusIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BulkCargoIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CandidateReductionConstraintIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CatalogueIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ChargeIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompletionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ConditionDeviationIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ConsolidatableIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContainerizedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CustomsImportClassifiedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DangerousGoodsApprovedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FreeOfChargeIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FrozenDocumentIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FullyPaidSharesIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.GeneralCargoIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.GovernmentAgreementConstraintIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.HazardousRiskIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.HumanFoodApprovedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.HumanFoodIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IndicationIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ItemUpdateRequestIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LegalStatusIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LivestockIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.MarkAttentionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.MarkCareIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.OnCarriageIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.OptionalLineItemIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.OrderableIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PartialDeliveryIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PowerIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PreCarriageIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PrepaidIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PricingUpdateRequestIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RefrigeratedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RefrigerationOnIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RequiredCurriculaIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReturnabilityIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReturnableMaterialIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SoleProprietorshipIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SpecialSecurityIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SplitConsignmentIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StatusAvailableIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxEvidenceIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxIncludedIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ThirdPartyPayerIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TimeDeviationIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ToOrderIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportServiceProviderCancellationIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportServiceProviderCompletionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportServiceProviderReadyForExecutionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportUserCancellationIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportUserCompletionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportUserReadyForExecutionIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UnknownPriceIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.VariantConstraintIndicatorType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * &lt;p&gt;Java class for IndicatorType complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="IndicatorType"&amp;gt;
 *   &amp;lt;simpleContent&amp;gt;
 *     &amp;lt;extension base="&amp;lt;http://www.w3.org/2001/XMLSchema&amp;gt;boolean"&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/simpleContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndicatorType", propOrder = {
    "value"
})
@XmlSeeAlso({
    AcceptedIndicatorType.class,
    AdValoremIndicatorType.class,
    AnimalFoodApprovedIndicatorType.class,
    AnimalFoodIndicatorType.class,
    AuctionConstraintIndicatorType.class,
    BackOrderAllowedIndicatorType.class,
    BalanceBroughtForwardIndicatorType.class,
    BasedOnConsensusIndicatorType.class,
    BulkCargoIndicatorType.class,
    CandidateReductionConstraintIndicatorType.class,
    CatalogueIndicatorType.class,
    ChargeIndicatorType.class,
    CompletionIndicatorType.class,
    ConditionDeviationIndicatorType.class,
    ConsolidatableIndicatorType.class,
    ContainerizedIndicatorType.class,
    CopyIndicatorType.class,
    CustomsImportClassifiedIndicatorType.class,
    DangerousGoodsApprovedIndicatorType.class,
    FreeOfChargeIndicatorType.class,
    FrozenDocumentIndicatorType.class,
    FullyPaidSharesIndicatorType.class,
    GeneralCargoIndicatorType.class,
    GovernmentAgreementConstraintIndicatorType.class,
    HazardousRiskIndicatorType.class,
    HumanFoodApprovedIndicatorType.class,
    HumanFoodIndicatorType.class,
    IndicationIndicatorType.class,
    ItemUpdateRequestIndicatorType.class,
    LegalStatusIndicatorType.class,
    LivestockIndicatorType.class,
    MarkAttentionIndicatorType.class,
    MarkCareIndicatorType.class,
    OnCarriageIndicatorType.class,
    OptionalLineItemIndicatorType.class,
    OrderableIndicatorType.class,
    PartialDeliveryIndicatorType.class,
    PowerIndicatorType.class,
    PreCarriageIndicatorType.class,
    PrepaidIndicatorType.class,
    PricingUpdateRequestIndicatorType.class,
    RefrigeratedIndicatorType.class,
    RefrigerationOnIndicatorType.class,
    RequiredCurriculaIndicatorType.class,
    ReturnabilityIndicatorType.class,
    ReturnableMaterialIndicatorType.class,
    SoleProprietorshipIndicatorType.class,
    SpecialSecurityIndicatorType.class,
    SplitConsignmentIndicatorType.class,
    StatusAvailableIndicatorType.class,
    TaxEvidenceIndicatorType.class,
    TaxIncludedIndicatorType.class,
    ThirdPartyPayerIndicatorType.class,
    TimeDeviationIndicatorType.class,
    ToOrderIndicatorType.class,
    TransportServiceProviderCancellationIndicatorType.class,
    TransportServiceProviderCompletionIndicatorType.class,
    TransportServiceProviderReadyForExecutionIndicatorType.class,
    TransportUserCancellationIndicatorType.class,
    TransportUserCompletionIndicatorType.class,
    TransportUserReadyForExecutionIndicatorType.class,
    UnknownPriceIndicatorType.class,
    VariantConstraintIndicatorType.class
})
public class IndicatorType {

    @XmlValue
    protected boolean value;

    /**
     * Gets the value of the value property.
     * 
     */
    public boolean isValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

}