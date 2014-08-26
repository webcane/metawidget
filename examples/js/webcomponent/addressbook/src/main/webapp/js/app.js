// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

var addressbook = addressbook || {};

( function() {

	'use strict';

	// Helper methods

	/**
	 * Simple implementation of an asynchronous JSON lookup.
	 */

	addressbook.fetchJSON = function( path, callback ) {

		var httpRequest = new XMLHttpRequest();
		httpRequest.onreadystatechange = function() {
			if ( httpRequest.readyState === 4 && httpRequest.status === 200 ) {
				callback( JSON.parse( httpRequest.responseText ) );
			}
		};
		httpRequest.open( 'GET', path );
		httpRequest.send();
	}

	addressbook.tableLayout = new metawidget.layout.HeadingTagLayoutDecorator( {
		delegate: new metawidget.layout.TableLayout( {
			tableStyleClass: "table-form",
			columnStyleClasses: [ "table-label-column", "table-component-column", "table-required-column" ],
			footerStyleClass: "buttons"
		} )
	} );

	// Prepare model

	addressbook.fetchJSON( 'js/contacts.json', function( data ) {

		addressbook.model = data;

		var summaryTableRows = document.getElementById( 'summary-table-rows' );
		summaryTableRows.model = {
			contacts: addressbook.model
		}
	} );

} )();